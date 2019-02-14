package com.wardziniak.aviation.processing.app.utils

import java.time.Duration
import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.streams.{KafkaStreams, Topology}

trait StreamApp extends LazyLogging {

  def runStreamWithConfiguration(topology: Topology, streamProperties: Properties): Unit = {
    val streams: KafkaStreams = new KafkaStreams(topology, streamProperties)
    streams.setUncaughtExceptionHandler((_: Thread, e: Throwable) => {
      logger.error("Uncaught exception in kafka stream", e)
    })
    Runtime.getRuntime.addShutdownHook(new Thread(() => {
      logger.info("Closing stream")
      streams.close(Duration.ofSeconds(10))
      logger.info("Stream closed")
    }))
    streams.start()
  }

}
