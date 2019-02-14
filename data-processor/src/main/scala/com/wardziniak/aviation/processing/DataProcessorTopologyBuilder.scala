package com.wardziniak.aviation.processing

import java.time.Duration
import java.util

import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.common.serialization.GenericSerde
import com.wardziniak.aviation.processing.DataProcessorTopologyBuilder.Container
import com.wardziniak.aviation.processing.internal.Cleaning.CleaningTransformer
import com.wardziniak.aviation.processing.internal.Deduplication.DeduplicationTransformer
import com.wardziniak.aviation.processing.internal.windows.CustomWindowedTransformerBuilder
import com.wardziniak.aviation.processing.internal.{ImputationTransformer, NormalizationTransformer}
import com.wardziniak.aviation.processing.storeNames._
import com.wardziniak.aviation.processing.topicNames._
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.{KeyValue, Topology}
import org.apache.kafka.streams.scala.kstream.Consumed
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}

import scala.collection.JavaConverters._


trait DataProcessorTopologyBuilder {


  def buildTopology: Topology = {
    val builder = new StreamsBuilder()


    // creation of needed store for Transformers and Processors

//    val deduplicationStore: StoreBuilder[KeyValueStore[String, FlightSnapshot]] =
//      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(DeduplicationStoreName), Serdes.String, GenericSerde[FlightSnapshot]())
//    builder.addStateStore(deduplicationStore)

    val stream = builder.stream[String, String]("input")(Consumed.`with`(Serdes.String, Serdes.String))

    CustomWindowedTransformerBuilder.aggregationWithCustomWindow[String, String, Container[String]] (stream, builder)(
      initializer = () => Container(List()),
      aggregator = (_, value, agg) => agg.copy(elements = value :: agg.elements),
      closingWindowPredicate = (_, _, agg) => agg.elements.size == 5,
      expirationTimeout = Duration.ofSeconds(60),
      keySerde = Serdes.String,
      aggValueSerde = GenericSerde[Container[String]]()
    ).flatMap[String, String]((k, value) => value.elements.map(v => (k, v)))
      .to("output")(Produced.`with`(Serdes.String, Serdes.String))

    builder.build()
  }

}

object DataProcessorTopologyBuilder {
  case class Container[T](elements: List[T])
}
