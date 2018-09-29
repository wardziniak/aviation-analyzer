package com.wardziniak.aviation.importer

import java.util.Properties

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.{GenericDeserializer, GenericSerializer}
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.libs.ws.{DefaultBodyWritables, StandaloneWSClient}

import scala.concurrent.ExecutionContext
import scala.io.Source

class FlightSnapshotDataImporterSpec(implicit ee: ExecutionEnv)
  extends Specification
    with EmbeddedKafka
    with DefaultBodyWritables {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val content: String = Source.fromResource("flights_data.json").mkString
  val wsclient = StandaloneFakeWSClient {
    case GET(url"http://localhost/get") => Ok(content)
  }

  val testTopic = "topic"

  "FlightSnapshotDataImporter" should {
    "extract key and publish to kafka" in {

      val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)

      withRunningKafkaOnFoundPort(userDefinedConfig) { actualConfig =>

        val props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, s"localhost:${actualConfig.kafkaPort}")
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")

        val testObject: FlightSnapshotDataImporter = new FlightSnapshotDataImporter {
          override val producer: KafkaProducer[String, FlightSnapshot] =
            new KafkaProducer[String, FlightSnapshot](props, new StringSerializer(), new GenericSerializer[FlightSnapshot])
          override val wsClient: StandaloneWSClient = wsclient

          implicit val executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
          implicit override val materializer: ActorMaterializer = mat
        }

        testObject.importData("http://localhost/get")(testTopic)

        val messages = consumeNumberKeyedMessagesFrom(topic = testTopic, number = 4, autoCommit = true)(actualConfig, new StringDeserializer(), new GenericDeserializer[FlightSnapshot])

        val flightLO706 = messages.filter(_._1 == "LOT706").head
        flightLO706._1 must beEqualTo("LOT706")
        flightLO706._2.flightNumber.icao must beEqualTo("LOT706")
        flightLO706._2.aircraft.icao must beEqualTo("E195")
      }
    }
  }
}
