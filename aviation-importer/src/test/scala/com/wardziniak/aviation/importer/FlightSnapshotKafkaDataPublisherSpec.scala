package com.wardziniak.aviation.importer

import java.util.Properties

import com.wardziniak.aviation.api.model._
import com.wardziniak.aviation.common.serialization.{GenericDeserializer, GenericSerializer}
import com.wardziniak.aviation.importer.external.FlightSnapshotKafkaDataPublisher
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.specs2.mutable._

import scala.concurrent.ExecutionContext


class FlightSnapshotKafkaDataPublisherSpec
  extends Specification
    with EmbeddedKafka {

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:6001")
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")


  val testTopic = "topic"

  "FlightSnapshotKafkaDataPublisher" should {
    "extract key and publish to kafka" in {

      withRunningKafka {
        createCustomTopic(testTopic)
        val testObject: FlightSnapshotKafkaDataPublisher = new FlightSnapshotKafkaDataPublisher {
          override val producer: KafkaProducer[String, FlightSnapshot] =
            new KafkaProducer[String, FlightSnapshot](props, new StringSerializer(), new GenericSerializer[FlightSnapshot])
          override val keyExtractor: FlightSnapshot => String = flight => flight.flightNumber.iata
          override implicit val executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
        }

        val flight = FlightSnapshot(
          localization = Localization(latitude = 42.6827, longitude = 28.7818, altitude = 10607, direction = 181),
          speed = Speed(horizontal = 746.46, isGround = false, vertical = -18.72),
          departure = AirportCode("WAW", "EPWA"),
          arrival = AirportCode("IST", "LTBA"),
          aircraft = Aircraft(regNumber = "SPLND", icao = "E195", icao24 = "48ADA3", iata = "E195"),
          flightNumber = FlightNumber(iata = "LO135", icao = "LOT135", number = "135"),
          airlineCode = AirlineCode(iata = "LO", icao = "LOT"),
          enRoute = "en-route",
          updated = 1537879920
        )

        testObject.publish(testTopic, List(flight))
        val message = consumeFirstKeyedMessageFrom(topic = testTopic, autoCommit = true)(EmbeddedKafkaConfig.defaultConfig, new StringDeserializer(), new GenericDeserializer[FlightSnapshot])

        message._1 must beEqualTo("LO135")
        message._2.flightNumber.iata must beEqualTo("LO135")
      }
    }
  }

}
