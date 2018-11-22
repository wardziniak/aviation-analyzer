package com.wardziniak.aviation.analyzer

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericSerde
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{Consumed, Produced}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import com.wardziniak.aviation.analyzer.Topics._

object InputTopicMErgerApp extends App with LazyLogging {

  val builder: StreamsBuilder = new StreamsBuilder()
  val source = builder.stream(RawInputTopic)( Consumed.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
  source.to(MainInputTopic)(Produced.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))

  val properties = new Properties()
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "looa1")
  properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.17:9092")
  properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
  properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
  val streams: KafkaStreams = new KafkaStreams(builder.build(), properties)
  streams.setUncaughtExceptionHandler((_: Thread, e: Throwable) => {
    logger.error("Uncaught exception in kafka stream", e)
  })

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
    logger.info("Closing stream")
    streams.close(10, TimeUnit.SECONDS)
    logger.info("Stream closed")
  }))

  streams.start()

}
