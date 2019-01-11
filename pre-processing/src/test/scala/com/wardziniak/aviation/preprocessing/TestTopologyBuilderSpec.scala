package com.wardziniak.aviation.preprocessing

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.TestDataBuilder.dirtyFlightSnapshot
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericSerializer
import com.wardziniak.aviation.preprocessing.TopicsNames.RawInputTopic
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.{StreamsConfig, TopologyTestDriver}
import org.apache.kafka.streams.test.ConsumerRecordFactory
import org.specs2.mutable.Specification

class TestTopologyBuilderSpec
  extends Specification
    with LazyLogging {


  "SomeTest" should {
    "some Test" in {
      val testBuilder = new TestTopologyBuilder{}

      val props: Properties = new Properties()
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
      props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/test1")
      val testDriver = new TopologyTestDriver(testBuilder.buildTopology, props)

      val factory: ConsumerRecordFactory[String, String] =
        new ConsumerRecordFactory[String, String]("input", new StringSerializer(), new StringSerializer())
      val rec1 = factory.create("input", "key1", "value")
      testDriver.pipeInput(rec1)

      val rec2 = factory.create("input", "key2", "valuAAA")
      testDriver.pipeInput(rec2)


      val store = testDriver.getKeyValueStore("storeName").asInstanceOf[KeyValueStore[String, String]]

      val test1 = store.get("key1")
      val test2 = store.get("key2")

      ok
    }
  }
}
