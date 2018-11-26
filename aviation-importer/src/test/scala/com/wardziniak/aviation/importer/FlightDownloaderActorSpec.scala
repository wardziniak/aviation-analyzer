package com.wardziniak.aviation.importer

import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.importer.api.FlightDownloadAction
import com.wardziniak.aviation.importer.flights.FlightDownloaderActor
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.libs.ws.DefaultBodyWritables

import scala.io.Source

class FlightDownloaderActorSpec (implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables
    with LazyLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val fileName = s"file:///${this.getClass.getResource("/flights_data.json").getPath}"
  val content: String = Source.fromURL(fileName).mkString
  //val content: String = Source.fromResource("flights_data.json").mkString
  val flightUrl: String = "https://aviation-edge.com/v2/public/flights?key=someKey"
  val wsClient = StandaloneFakeWSClient {
    case GET(url"$flightUrl") => Ok(content)
  }

  var messageCounter = 0
  case class PublisherActor() extends Actor with LazyLogging {
    override def receive: Receive = {
      case _: FlightSnapshot => messageCounter = messageCounter + 1
      case p => logger.error(s"Unknown message [$p]")
    }
  }

  "FlightSnapshotDataDownloader" should {
    "parse json properly" in {
      val publisherActor = system.actorOf(Props(PublisherActor()), name = "flightPublisher")
      val downloaderActor = system.actorOf(Props(FlightDownloaderActor(secretKey = "someKey", wsClient = wsClient)))
      downloaderActor ! FlightDownloadAction
      // TODO Remove that sleep and make test deterministic
      Thread.sleep(2000)
      messageCounter must beEqualTo(4)
    }
  }

}
