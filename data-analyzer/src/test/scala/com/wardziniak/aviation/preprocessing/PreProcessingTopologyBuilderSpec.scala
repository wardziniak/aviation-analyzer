package com.wardziniak.aviation.preprocessing

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.TestDataBuilder
import com.wardziniak.aviation.analyzer.Topics.{ErrorTopic, RawInputTopic}
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.{GenericDeserializer, GenericSerializer}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.streams.test.ConsumerRecordFactory
import org.specs2.mutable.Specification
import org.apache.kafka.streams.{StreamsConfig, TopologyTestDriver}

class PreProcessingTopologyBuilderSpec
  extends Specification
    with LazyLogging {

  "PreProcessingTopologyBuilder" should {
    "push dirty messages to error topic" in {
      val testedObject = new PreProcessingTopologyBuilder{}

      val props: Properties = new Properties()
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
      val testDriver = new TopologyTestDriver(testedObject.buildTopology, props)

      val factory: ConsumerRecordFactory[String, FlightSnapshot] =
        new ConsumerRecordFactory[String, FlightSnapshot](RawInputTopic, new StringSerializer(), GenericSerializer[FlightSnapshot]())
      val flight = factory.create(RawInputTopic, TestDataBuilder.dirtyFlightSnapshot.flightNumber.iata, TestDataBuilder.dirtyFlightSnapshot)
      testDriver.pipeInput(flight)

      val dirtyFlight = testDriver.readOutput(ErrorTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]()).value()

      dirtyFlight must beEqualTo(TestDataBuilder.dirtyFlightSnapshot)
    }
  }
}
