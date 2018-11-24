package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.TopologyBuilder
import com.wardziniak.aviation.analyzer.Stores.{InAirFlightStoreName, LandedFlightStoreName}
import com.wardziniak.aviation.analyzer.Topics._
import com.wardziniak.aviation.api.model.{Airport, AnalyticFlightSnapshot, FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.common.serialization.GenericSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}

trait PreProcessingTopologyBuilder
  extends TopologyBuilder
    with LazyLogging {
  override def buildTopology: Topology = {
    val builder: StreamsBuilder = new StreamsBuilder()

    // Store for snapshots for flights, that are in air
    val flightsInAirStore: StoreBuilder[KeyValueStore[String,InAirFlightData]] =
      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(InAirFlightStoreName), Serdes.String(), GenericSerde[InAirFlightData]())
    builder.addStateStore(flightsInAirStore)

    val streamsArray = builder.stream(RawInputTopic)(Consumed.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
      .filter((_, flightInfo) => flightInfo.airlineCode.iata == "LH")
      .branch(Helpers.isClean, Helpers.allRecords)

    val (source, errorStream) = (streamsArray(0), streamsArray(1))

    val landedMaterialized: Materialized[String, FlightSnapshot, KeyValueStore[Bytes, Array[Byte]]] = Materialized.as[String, FlightSnapshot, KeyValueStore[Bytes, Array[Byte]]](LandedFlightStoreName)

    val airports = builder.table(AirportsTopic)(Consumed.`with`(Serdes.String(), new GenericSerde[Airport]))

    val landedStream = builder.table(LandedTableTopic, landedMaterialized)(Consumed.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
      .toStream
      .peek((key, value) => logger.debug(s"[landed  [$key], [$value]"))
      .map((_, flightSnapshot) => (flightSnapshot.arrival.iata, flightSnapshot))
      .join(airports)((landedSnapshot, airport) => (landedSnapshot, airport))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[Airport]))
      .mapValues(Helpers.calculateLandingTime _)
      .map((_, flight) => (flight.flightNumber.iata, flight))

    val inAirAfterLanding = source.transform[String, FlightSnapshot](InAirTransformer(InAirFlightStoreName, LandedFlightStoreName), InAirFlightStoreName, LandedFlightStoreName)
    inAirAfterLanding
      .join(landedStream)((f, landedFlight) => f.witLandedTimestamp(landedFlight.landedTimestamp.getOrElse(-1)), JoinWindows.of(1000))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[FlightSnapshot]))
      .map((_, fs) => (fs.arrival.iata, fs))
      .join(airports)((fs, airport) => fs.withAirportData(airport))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[Airport]))
      .peek((key, value) => logger.debug(s"afterJoing: [$key][$value]"))
      .to(InAirWithLandedDataTopic)(Produced.`with`(Serdes.String(), new GenericSerde[AnalyticFlightSnapshot]))
      //.to(InAirWithLandedDataTopic)(Produced.`with`(Serdes.String(), new GenericSerde[AnalyticFlightSnapshot]))


    errorStream.peek((key, f) => logger.debug(s"[key=$key], [value=$f")).to(ErrorTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))

    builder.build()
  }
}
