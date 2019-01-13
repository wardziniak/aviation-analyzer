package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{Airport, AnalyticFlightSnapshot, FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.common.serialization.GenericSerde
import com.wardziniak.aviation.preprocessing.StoresNames.{AirportsStoreName, InAirFlightStoreName}
import com.wardziniak.aviation.preprocessing.TopicsNames._
import com.wardziniak.aviation.preprocessing.utils.TopologyBuilder
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

    // Parsing data from raw topic
    val streamsArray = builder.stream(RawInputTopic)(Consumed.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
      //.filter((_, flightInfo) => flightInfo.airlineCode.iata == "LH")
      .branch(Helpers.isClean, Helpers.allRecords)
    val (source, errorStream) = (streamsArray(0), streamsArray(1))
    // Airports
    val airportsMaterialized: Materialized[String, Airport, KeyValueStore[Bytes, Array[Byte]]] =
      Materialized.as[String, Airport, KeyValueStore[Bytes, Array[Byte]]](AirportsStoreName)
    airportsMaterialized.withKeySerde(Serdes.String())
    airportsMaterialized.withValueSerde(new GenericSerde[Airport])
    builder.table(AirportsTopic)(Consumed.`with`(Serdes.String(), new GenericSerde[Airport]))
      .filter(Helpers.isEuropeAirport, airportsMaterialized)

    //builder.table(AirportsTopic, airportsMaterialized)(Consumed.`with`(Serdes.String(), new GenericSerde[Airport]))


    source.transform[String, AnalyticFlightSnapshot](() => InAirTransformer(InAirFlightStoreName, AirportsStoreName), InAirFlightStoreName, AirportsStoreName)
      .peek((key, value) => logger.info(s"AnalyticFlightSnapshot:[$key], [$value]"))
      .to(InAirWithLandedDataTopic)(Produced.`with`(Serdes.String(), new GenericSerde[AnalyticFlightSnapshot]))

    errorStream.peek((key, f) => logger.trace(s"[key=$key], [value=$f")).to(ErrorTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
    builder.build()
  }
}
