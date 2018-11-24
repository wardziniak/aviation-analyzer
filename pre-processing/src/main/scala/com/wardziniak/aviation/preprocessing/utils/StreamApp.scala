package com.wardziniak.aviation.preprocessing.utils

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.streams.{KafkaStreams, Topology}

trait StreamApp extends LazyLogging {
  self: TopologyBuilder =>

  def runStreamWithConfiguration(topology: Topology, streamProperties: Properties): Unit = {
    val streams: KafkaStreams = new KafkaStreams(topology, streamProperties)
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

}
