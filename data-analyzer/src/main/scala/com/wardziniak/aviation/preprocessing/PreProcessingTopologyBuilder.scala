package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.TopologyBuilder
import com.wardziniak.aviation.analyzer.Stores.{InAirFlightStoreName, LandedFlightStoreName}
import com.wardziniak.aviation.analyzer.Topics._
import com.wardziniak.aviation.api.model.{Airport, FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.common.serialization.GenericSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.{Consumed, Joined, Materialized, Produced}
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

    builder.table(LandedTableTopic, landedMaterialized)(Consumed.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
      .toStream
      .peek((key, value) => logger.info(s"[landed  key=$key, value=$value]"))
      .map((_, flightSnapshot) => (flightSnapshot.arrival.iata, flightSnapshot))
      .join(airports)((landedSnapshot, airport) => (landedSnapshot, airport))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[Airport]))
      .mapValues(Helpers.calculateLandingTime _)
      .map((_, flight) => (flight.flightNumber.iata, flight))
      .to(LandedTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))

    val inAirAfterLanding = source.transform[String, FlightSnapshot](InAirTransformer(InAirFlightStoreName, LandedFlightStoreName), InAirFlightStoreName, LandedFlightStoreName)
    val landedTable = builder.table(LandedTopic)(Consumed.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
    inAirAfterLanding
      .join(landedTable)((f, landedFlight) => f.witLandedTimestamp(landedFlight.landedTimestamp.getOrElse(-1)))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[FlightSnapshot]))
      .to(InAirWithLandTimeTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
    errorStream.peek((key, f) => logger.info(s"[key=$key], [value=$f")).to(ErrorTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))

    builder.build()
  }
}
