package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.preprocessing.StoresNames.{InAirFlightStoreName, LandedFlightStoreName}
import com.wardziniak.aviation.preprocessing.TopicsNames._
import com.wardziniak.aviation.api.model.{Airport, AnalyticFlightSnapshot, FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.common.serialization.GenericSerde
import com.wardziniak.aviation.preprocessing.utils.TopologyBuilder
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.{KeyValue, Topology}
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.scala.{StreamsBuilder, kstream}
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}

trait PreProcessingTopologyBuilder
  extends TopologyBuilder
    with LazyLogging {
  override def buildTopology: Topology = {
    val builder: StreamsBuilder = new StreamsBuilder()

    val serdes = GenericSerde[InAirFlightData]()
    // Store for snapshots for flights, that are in air
    val flightsInAirStore: StoreBuilder[KeyValueStore[String,InAirFlightData]] =
      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(InAirFlightStoreName), Serdes.String(), GenericSerde[InAirFlightData]())
    builder.addStateStore(flightsInAirStore)

    val streamsArray = builder.stream(RawInputTopic)(Consumed.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
      //.filter((_, flightInfo) => flightInfo.airlineCode.iata == "LH")
      .branch(Helpers.isClean, Helpers.allRecords)

    val (source, errorStream) = (streamsArray(0), streamsArray(1))

    val landedMaterialized: Materialized[String, FlightSnapshot, KeyValueStore[Bytes, Array[Byte]]] = Materialized.as[String, FlightSnapshot, KeyValueStore[Bytes, Array[Byte]]](LandedFlightStoreName)

    val airports = builder.table(AirportsTopic)(Consumed.`with`(Serdes.String(), new GenericSerde[Airport]))

    val landedStream: kstream.KStream[String, FlightSnapshot] = builder.table(LandedTableTopic, landedMaterialized)(Consumed.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
      .toStream
      .peek((key, value) => logger.info(s"[landed  [$key], [$value]"))
      .map((_, flightSnapshot) => (flightSnapshot.arrival.iata, flightSnapshot))
//      .peek((key, value) => logger.info(s"[landedPoMap::[$key], [$value]"))
      .join(airports)((landedSnapshot, airport) => (landedSnapshot, airport))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[Airport]))
//      .peek((key, value) => logger.info(s"[landedStream::[$key], [$value]"))
      .mapValues(Helpers.calculateLandingTime _)
      .map((_, flight) => (flight.flightNumber.iata, flight))
      //.selectKey((key, _ ) => key )
      //.through(IntermediateLandedTableTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
      .peek((key, value) => logger.info(s"landedStream::lastP:[$key], [$value]"))



    //airports.toStream.to("some_results")(Produced.`with`(Serdes.String(), new GenericSerde[Airport]))

    val inAirAfterLanding = source.transform[String, FlightSnapshot](() => InAirTransformer(InAirFlightStoreName, LandedFlightStoreName), InAirFlightStoreName, LandedFlightStoreName)
    //inAirAfterLanding.selectKey((key, _ ) => key ).through(IntermediateManTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
        .peek((key, value) => logger.info(s"inAirAfterLanding::1Peek:[$key], [$value]"))
      .join(landedStream)((f, landedFlight) => {
//        logger.info(s"[inAirAfterLanding::join:map::[$f], [$landedFlight]")
        f.witLandedTimestamp(landedFlight.landedTimestamp.getOrElse(-1))
      }, JoinWindows.of(60000))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[FlightSnapshot]))
      .peek((key, value) => logger.info(s"[inAirAfterLanding::hall:map::[$key], [$value]"))
      .map((_, fs) => (fs.arrival.iata, fs))
//      .peek((key, value) => logger.info(s"[inAirAfterLanding::join:map::[$key], [$value]"))
      .join(airports)((fs, airport) => fs.withAirportData(airport))(Joined.`with`(Serdes.String(), new GenericSerde[FlightSnapshot], new GenericSerde[Airport]))
      .peek((key, value) => logger.info(s"afterJoing: [$key][$value]"))
      .map((_, value) => (value.flightNumber.iata, value))
      .to(InAirWithLandedDataTopic)(Produced.`with`(Serdes.String(), new GenericSerde[AnalyticFlightSnapshot]))
      //.to(InAirWithLandedDataTopic)(Produced.`with`(Serdes.String(), new GenericSerde[AnalyticFlightSnapshot]))


    errorStream.peek((key, f) => logger.info(s"[key=$key], [value=$f")).to(ErrorTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))

    builder.build()
  }
}
