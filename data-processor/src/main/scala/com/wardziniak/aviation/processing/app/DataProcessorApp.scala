package com.wardziniak.aviation.processing.app

import com.wardziniak.aviation.processing.DataProcessorTopologyBuilder
import com.wardziniak.aviation.processing.app.utils.{StreamApp, StreamConfigBuilder}
import org.apache.kafka.streams.Topology

object DataProcessorApp
  extends App
    with StreamApp with DataProcessorTopologyBuilder {

  val preProcessingAppConfig = StreamConfigBuilder()
    .withApplicationId(applicationId = "processing1")
    .withBootstrapServer(bootstrapServer = "localhost:9092").build
  val applicationTopology: Topology = buildTopology

  runStreamWithConfiguration(topology = applicationTopology, streamProperties = preProcessingAppConfig)

}
