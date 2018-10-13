package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericDeserializer
import com.wardziniak.aviation.importer.flights.FlightSnapshotDataImporter
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.common.serialization.StringDeserializer
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.libs.ws.{DefaultBodyWritables, StandaloneWSClient}

import scala.concurrent.ExecutionContext
import scala.io.Source
import scala.language.reflectiveCalls

class FlightSnapshotDataImporterSpec(implicit ee: ExecutionEnv)
  extends Specification
    with EmbeddedKafka
    with DefaultBodyWritables {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val content: String = Source.fromResource("flights_data.json").mkString
  val flightUrl: String = "http://aviation-edge.com/v2/public/flights?key=someKey"
  val wsclient = StandaloneFakeWSClient {
    case GET(url"$flightUrl") => Ok(content)
  }

  val testTopic = "topic"

  "FlightSnapshotDataImporter" should {
    "extract key and publish to kafka" in {

      val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)

      withRunningKafkaOnFoundPort(userDefinedConfig) { actualConfig =>

        val testObject: FlightSnapshotDataImporter = new FlightSnapshotDataImporter {
          override def kafkaServer: String = s"localhost:${actualConfig.kafkaPort}"
          override val wsClient: StandaloneWSClient = wsclient

          implicit val executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
          implicit override val materializer: ActorMaterializer = mat
        }

        testObject.importData(flightUrl)(testTopic)

        val messages = consumeNumberKeyedMessagesFrom(topic = testTopic, number = 4, autoCommit = true)(actualConfig, new StringDeserializer(), new GenericDeserializer[FlightSnapshot])

        val flightLO706 = messages.filter(_._1 == "LOT706").head
        flightLO706._1 must beEqualTo("LOT706")
        flightLO706._2.flightNumber.icao must beEqualTo("LOT706")
        flightLO706._2.aircraft.icao must beEqualTo("E195")
      }
    }
  }
}
