package com.wardziniak.aviation.importer

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.api.model._
import com.wardziniak.aviation.common.serialization.GenericDeserializer
import com.wardziniak.aviation.importer.flights.FlightDataPublisherActor
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.common.serialization.StringDeserializer
import org.specs2.mutable.Specification

class FlightDataPublisherActorSpec
  extends Specification with EmbeddedKafka {

  "FlightSnapshotPublisherActor" should {
    "publish messages to Kafka" in {

      implicit val system: ActorSystem = ActorSystem()
      implicit val mat: ActorMaterializer = ActorMaterializer()
      val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)
      val testTopic = "topic"

      withRunningKafkaOnFoundPort(userDefinedConfig) { actualConfig =>

        val publisher = system
          .actorOf(Props(FlightDataPublisherActor(kafkaServer = s"localhost:${actualConfig.kafkaPort}", topic = testTopic)))

        val flight1 = FlightSnapshot(
          localization = Localization(latitude = 11.1, longitude = 34.53, altitude = 11100, direction = 124),
          speed = Speed(horizontal = 11.1, isGround = false, vertical = 1000.1),
          departure = AirportCode(iata = "WAW", icao = "WA"),
          arrival = AirportCode(iata = "JFK", icao = "JF"),
          aircraft = AircraftCode(regNumber = "121f", icao = "PL123", icao24 = "PL123", iata = "PL123"),
          flightNumber = FlightNumber(iata = "LO123", icao = "LOT123", number = "123"),
          airlineCode = AirlineCode(iata = "LO", icao = "LOT"),
          enRoute = "String",
          updated = 111)

        publisher ! flight1

        val messages = consumeNumberKeyedMessagesFrom(topic = testTopic, number = 1, autoCommit = true)(actualConfig, new StringDeserializer(), new GenericDeserializer[FlightSnapshot])
        messages.size must beEqualTo(1)
        messages.head._1 must beEqualTo("LOT123")
      }
    }
  }
}
