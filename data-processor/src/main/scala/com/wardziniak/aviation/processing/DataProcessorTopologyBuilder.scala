package com.wardziniak.aviation.processing

import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.common.serialization.GenericSerde
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, KStream, Produced}
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}
import com.wardziniak.aviation.processing.storeNames._
import com.wardziniak.aviation.processing.topicNames._
import java.time.{Duration => JDuration}

import com.wardziniak.aviation.processing.internal.Deduplication.DeduplicationTransformer
import com.wardziniak.aviation.processing.internal.Cleaning.CleaningTransformer


trait DataProcessorTopologyBuilder {

  def buildTopology: Topology = {
    val expirationTimeout: JDuration = JDuration.ofMinutes(20)

    val builder = new StreamsBuilder()
    val flightGrouperStore: StoreBuilder[KeyValueStore[FlightNumberIata, InAirFlightData]] =
      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(FlightGrouperStoreName), GenericSerde[FlightNumberIata](), GenericSerde[InAirFlightData]())
    builder.addStateStore(flightGrouperStore)

//    builder
//      .stream(RawDataInputTopic)(Consumed.`with`(GenericSerde[FlightNumberIata](), GenericSerde[FlightSnapshot]()))
//      .transform(() => FlightGrouperTransformer(expirationTimeout, FlightGrouperStoreName), FlightGrouperStoreName)
//      .to(FlightGroupedTopic)(Produced.`with`(GenericSerde[FlightNumberIata](), GenericSerde[InAirFlightData]()))
//
//    builder.stream(FlightGroupedTopic)(Consumed.`with`(GenericSerde[FlightNumberIata](), GenericSerde[InAirFlightData]()))
//      .transformValues(() => DeduplicationTransformer())
//      .transformValues(() => CleaningTransformer())
//    builder.build()
    ???
  }
}

object DataProcessorTopologyBuilder {
  case class Container[T](elements: List[T])
}
