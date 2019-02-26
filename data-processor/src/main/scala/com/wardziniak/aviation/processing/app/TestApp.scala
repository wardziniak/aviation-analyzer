package com.wardziniak.aviation.processing.app

import java.time.Duration

import com.wardziniak.aviation.processing.app.DataProcessorApp.{buildTopology, runStreamWithConfiguration}
import com.wardziniak.aviation.processing.app.utils.{StreamApp, StreamConfigBuilder}
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KeyValue, StreamsBuilder, Topology}
import org.apache.kafka.streams.kstream._

object TestApp extends App with StreamApp {


  val preProcessingAppConfig = StreamConfigBuilder()
    .withApplicationId(applicationId = "processing1")
    .withBootstrapServer(bootstrapServer = "localhost:9092").build
  val applicationTopology: Topology = {
    val builder = new StreamsBuilder()

//    val mat = Materialized.`with`(Serdes.String, Serdes.String)

    val rawInputData: KStream[String, String] = builder
      .stream("input", Consumed.`with`(Serdes.String, Serdes.String))


    val ddd = rawInputData
      .groupByKey(Grouped.`with`(Serdes.String, Serdes.String))
      .windowedBy(SessionWindows.`with`(Duration.ofSeconds(20)).grace(Duration.ofMillis(100)))
      .aggregate[String](new Initializer[String] {
      override def apply(): String = ""
    }, new Aggregator[String, String, String] {
      override def apply(key: String, value: String, aggregate: String): String = s"${aggregate}_$value"
    }, new Merger[String, String] {
      override def apply(aggKey: String, aggOne: String, aggTwo: String): String = s"$aggOne.$aggTwo"
    })
//    .reduce((value1, value2) => s"${value1}_$value2")
      .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
      .toStream.map[String, String]((key, value) => new KeyValue(key.key(), value))
    ddd.to("outputSuppress")


//        (_, value, agg) => s"${agg}_$value", (_, agg1, agg2) => s"$agg1.$agg2",
//        Materialized.as("aggregated-stream-store").withValueSerde(Serdes.String))
//      .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
//      .toStream.to("outputSuppress")
    builder.build()
  }

  runStreamWithConfiguration(topology = applicationTopology, streamProperties = preProcessingAppConfig)
}
