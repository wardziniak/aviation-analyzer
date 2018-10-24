package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
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

  val content: String = Source.fromResource("flights_data.json").mkString
  val flightUrl: String = "http://aviation-edge.com/v2/public/flights?key=someKey"
  val wsClient = StandaloneFakeWSClient {
    case GET(url"$flightUrl") => Ok(content)
  }

  "FlightSnapshotDataDownloader" should {
    "parse json properly" in {
      ok
    }
  }

}
