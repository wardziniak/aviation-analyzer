package com.wardziniak.aviation.preprocessing

import java.io.InputStream
import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.TestDataBuilder._
import com.wardziniak.aviation.analyzer.Topics._
import com.wardziniak.aviation.api.model.{Airport, FlightSnapshot}
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
      val testDriver = new TopologyTestDriver(testedObject.buildTopology, props)

      val factory: ConsumerRecordFactory[String, FlightSnapshot] =
        new ConsumerRecordFactory[String, FlightSnapshot](RawInputTopic, new StringSerializer(), GenericSerializer[FlightSnapshot]())
      val flight = factory.create(RawInputTopic, dirtyFlightSnapshot.flightNumber.iata, dirtyFlightSnapshot)
      testDriver.pipeInput(flight)

      val dirtyFlight = testDriver.readOutput(ErrorTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]()).value()

      testDriver.close()
      dirtyFlight must beEqualTo(dirtyFlightSnapshot)
    }

    "reading test" in {

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

      logger.info(s"test: landed $landedSnapshot")

//      val snapshotWithLandedTime = testDriver.readOutput(InAirWithLandTimeTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]()).value()

      lazy val recordStream: Stream[ProducerRecord[String, FlightSnapshot]] =
        MessageStreamReader.messageStream[String, FlightSnapshot](InAirWithLandTimeTopic, testDriver, new StringDeserializer(), GenericDeserializer[FlightSnapshot]())


      recordStream.toList.foreach(f =>
        logger.info(s"F: ${f.value().updated}, ${f.value().landedTimestamp}, $f")
      )

        //testDriver.readOutput(InAirWithLandTimeTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]()).value()) #::  testDriver.readOutput(InAirWithLandTimeTopic, new StringDeserializer(), GenericDeserializer[FlightSnapshot]()).value())

//      def stream[K <: Offer](uri: String)(loader: PageLoader)(pageNavigator: PageNavigator, dataExtractor: DataExtractor[K]): Stream[K] = {
//        val orDocument = loader.loadDocument(uri)
//        orDocument.map(document =>
//          Stream(dataExtractor.extractOffers(document.body()): _*) #::: pageNavigator.getNextPageUri(document).map(nextPageUri => stream(nextPageUri)(loader)(pageNavigator, dataExtractor)).getOrElse(Stream.empty)
//        ).getOrElse(Stream.empty)
//      }

      //val fraNceFlightsSnapshots = Json.parseStringAs[List[FlightSnapshot]](flightString)

      testDriver.close()
      ok
    }
  }
}
