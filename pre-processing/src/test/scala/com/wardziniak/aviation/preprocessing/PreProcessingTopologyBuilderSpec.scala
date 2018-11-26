package com.wardziniak.aviation.preprocessing

import java.io.InputStream
import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.TestDataBuilder._
import com.wardziniak.aviation.preprocessing.TopicsNames._
import com.wardziniak.aviation.api.model.{Airport, AnalyticFlightSnapshot, FlightSnapshot}
import com.wardziniak.aviation.common.serialization.{GenericDeserializer, GenericSerializer}
import com.wardziniak.aviation.utils.MessageStreamReader
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.streams.test.ConsumerRecordFactory
import org.apache.kafka.streams.{StreamsConfig, TopologyTestDriver}
import org.specs2.mutable.Specification

class PreProcessingTopologyBuilderSpec
  extends Specification
    with LazyLogging {

  "PreProcessingTopologyBuilder" should {

    "push dirty messages to error topic" in {
      val testedObject = new PreProcessingTopologyBuilder{}

      val props: Properties = new Properties()
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
      props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/test1")
      val testDriver = new TopologyTestDriver(testedObject.buildTopology, props)

      val factory: ConsumerRecordFactory[String, FlightSnapshot] =
        new ConsumerRecordFactory[String, FlightSnapshot](RawInputTopic, new StringSerializer(), GenericSerializer[FlightSnapshot]())
      val flight = factory.create(RawInputTopic, dirtyFlightSnapshot.flightNumber.iata, dirtyFlightSnapshot)
      testDriver.pipeInput(flight)

      val dirtyFlight = testDriver.readOutput(ErrorTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]()).value()

      testDriver.close()
      dirtyFlight must beEqualTo(dirtyFlightSnapshot)
    }

    "Transform raw data to flight data with landing info" in {

      import org.json4s._
      import org.json4s.jackson._
      implicit val formats: DefaultFormats.type = DefaultFormats

      val stream: InputStream = getClass.getResourceAsStream("/fraNceFlightsSnapshots.json")
      val flightString: String = scala.io.Source
        .fromInputStream(stream).mkString
      val flights = JsonMethods.parse(flightString).extract[List[FlightSnapshot]]


      val testedObject = new PreProcessingTopologyBuilder{}

      val props: Properties = new Properties()
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
      props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/test2")
      val testDriver: TopologyTestDriver = new TopologyTestDriver(testedObject.buildTopology, props)

      import collection.JavaConverters._

      val flightFactory: ConsumerRecordFactory[String, FlightSnapshot] =
        new ConsumerRecordFactory[String, FlightSnapshot](new StringSerializer(), GenericSerializer[FlightSnapshot]())
      val flightConsumerRecords = flights.map(f => flightFactory.create(RawInputTopic, f.flightNumber.iata, f)).asJava

      val airportFactory: ConsumerRecordFactory[String, Airport] =
        new ConsumerRecordFactory[String, Airport](new StringSerializer(), GenericSerializer[Airport]())

      testDriver.pipeInput(flightConsumerRecords)
      testDriver.pipeInput(airportFactory.create(AirportsTopic, departureFrankfurtAirport.codeIata, departureFrankfurtAirport))
      testDriver.pipeInput(airportFactory.create(AirportsTopic, arrivalNiceAirport.codeIata, arrivalNiceAirport))


      testDriver.advanceWallClockTime(60000)

      val landedSnapshot = testDriver.readOutput(LandedTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]())

      lazy val recordStream: Stream[ProducerRecord[String, AnalyticFlightSnapshot]] =
        MessageStreamReader.messageStream[String, AnalyticFlightSnapshot](InAirWithLandedDataTopic, testDriver, new StringDeserializer(), GenericDeserializer[AnalyticFlightSnapshot]())


      val flightsWithLandingTime = recordStream.toList

      flightsWithLandingTime.size must beEqualTo(5)
      val landingTimes = flightsWithLandingTime
        .map(_.value())
        .flatMap(_.landedTimestamp)
      landingTimes.size must beEqualTo(5)
      landingTimes must eachOf(1542664182L)
      flightsWithLandingTime.map(_.value().arrivalAirport) must eachOf(arrivalNiceAirport)
      testDriver.close()
      ok
    }
  }
}
