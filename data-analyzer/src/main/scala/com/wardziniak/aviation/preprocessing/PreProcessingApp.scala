package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.StreamApp
import com.wardziniak.aviation.analyzer.kafka.StreamConfigBuilder
import org.apache.kafka.streams.Topology


/**
  * Application pre process data for analytic purpose
  * In [[com.wardziniak.aviation.analyzer.Topics.InAirWithLandTimeTopic]]
  * FlightSnapshot records with time to land are saved
  * New approach to calculating landing time for plane
  * If for some LANDING_TIMEOUT there is no new entry for flight
  * it assume that plane has landed and last entry updateTime + alpha is landing time
  *
  */
object PreProcessingApp
  extends App
    with StreamApp
    with PreProcessingTopologyBuilder
    with LazyLogging {

  val TimeDifferenceMs = 300000

  val preProcessingAppConfig = StreamConfigBuilder()
    .withApplicationId(applicationId = "pre-processing1")
    .withBootstrapServer(bootstrapServer = "192.168.1.17:9092").build
  val applicationTopology: Topology = buildTopology

  runStreamWithConfiguration(topology = applicationTopology, streamProperties = preProcessingAppConfig)
}
