package com.wardziniak.aviation.processing

import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.common.serialization.GenericSerde
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.scala.kstream.{Consumed, KStream, Produced}
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}
import com.wardziniak.aviation.processing.storeNames._
import com.wardziniak.aviation.processing.topicNames._
import com.wardziniak.aviation.processing.nodeName._
import java.time.{Duration => JDuration}

import com.wardziniak.aviation.processing.internal.Cleaning.CleaningProcessor
import com.wardziniak.aviation.processing.internal.Deduplication.DeduplicationProcessor
import com.wardziniak.aviation.processing.internal.FlightGrouper.FlightGrouperProcessor
import com.wardziniak.aviation.processing.internal.Imputation.ImputationProcessor
import org.apache.kafka.streams.processor.{Processor, ProcessorSupplier}
import org.apache.kafka.streams.state.Stores
import org.apache.kafka.streams.state.internals.KeyValueStoreBuilder


trait DataProcessorTopologyBuilder {

  def buildTopology: Topology = {
    /**
      *  Topologies:
      *  ub-topology: 0
      *  Source: RawSource (topics: [in-raw])
      *    --> FlightGrouperProcessor
      *  Processor: FlightGrouperProcessor (stores: [flight-grouper-store])
      *    --> CleaningProcessor
      *    <-- RawSource
      *  Processor: CleaningProcessor (stores: [])
      *    --> DeduplicationProcessor
      *    <-- FlightGrouperProcessor
      *  Processor: DeduplicationProcessor (stores: [])
      *    --> ImputationProcessor
      *    <-- CleaningProcessor
      *  Processor: ImputationProcessor (stores: [])
      *    --> NormalizationProcessor
      *    <-- DeduplicationProcessor
      *  Processor: NormalizationProcessor (stores: [])
      *    --> SinkProcessor
      *    <-- ImputationProcessor
      *  Sink: SinkProcessor (topic: processed-data)
      *    <-- NormalizationProcessor
      *
      **/


    val expirationTimeout: JDuration = JDuration.ofMinutes(20)

    val builder = new StreamsBuilder()
    val topology = builder.build()

    val flightGrouperStoreSupplier = Stores.persistentKeyValueStore(FlightGrouperStoreName)
    val flightGrouperStoreBuilder = Stores.keyValueStoreBuilder(flightGrouperStoreSupplier, GenericSerde[FlightNumberIata](), GenericSerde[InAirFlightData]())

    topology.addSource(RawSourceNodeName, RawDataInputTopic)
    // RawSourceNodeName -> FlightGrouperProcessorNodeName
    val flightGrouperProcessorSupplier: ProcessorSupplier[FlightNumberIata, FlightSnapshot] = () => FlightGrouperProcessor(expirationTimeout, FlightGrouperStoreName)
    val cleaningGrouperProcessorSupplier: ProcessorSupplier[FlightNumberIata, InAirFlightData] = () => CleaningProcessor()
    val deduplicationProcessorSupplier: ProcessorSupplier[FlightNumberIata, InAirFlightData] = () => DeduplicationProcessor()
    // ???
    val normalizationProcessorSupplier: ProcessorSupplier[FlightNumberIata, InAirFlightData] = () => DeduplicationProcessor()
    val imputationProcessorSupplier: ProcessorSupplier[FlightNumberIata, InAirFlightData] = () => ImputationProcessor(???)

    topology.addProcessor(FlightGrouperProcessorNodeName,  flightGrouperProcessorSupplier, RawSourceNodeName)
    topology.addProcessor(CleaningProcessorNodeName, cleaningGrouperProcessorSupplier, FlightGrouperProcessorNodeName)
    topology.addProcessor(DeduplicationProcessorNodeName, deduplicationProcessorSupplier, CleaningProcessorNodeName)
    topology.addProcessor(ImputationProcessorNodeName, imputationProcessorSupplier, DeduplicationProcessorNodeName)
    topology.addProcessor(NormalizationProcessorNodeName, normalizationProcessorSupplier, ImputationProcessorNodeName)
    topology.addSink(SinkProcessorNodeName, ProcessedDataTopic, NormalizationProcessorNodeName)

    topology.addStateStore(flightGrouperStoreBuilder, FlightGrouperProcessorNodeName)
    println(topology.describe())
    topology
  }
}

object DataProcessorTopologyBuilder {
  case class Container[T](elements: List[T])
}
