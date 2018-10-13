package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.importer.aircraft.{AircraftDTO, AircraftDataDownloader}
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import play.api.libs.ws.DefaultBodyWritables

import scala.concurrent.Future
import scala.io.Source
import scala.language.reflectiveCalls

class AircraftCodeDataDownloaderSpec(implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables with AircraftDataDownloader
    with LazyLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val content: String = Source.fromResource("aircrafts_data.json").mkString
  val flightUrl: String = "http://aviation-edge.com/v2/public/flights?key=someKey"
  val wsClient = StandaloneFakeWSClient {
    case GET(url"$flightUrl") => Ok(content)
  }

  "AircraftDataDownloader" should {

    "parse json properly" in {
      // test
      val data = download(url = flightUrl)

      // validation
      val dAIMAAircraft: Future[Option[AircraftDTO]] = data.map(t => t.find(_.numberRegistration == "D-AIMA"))
      data.map(_.size) must beEqualTo(269).await
      dAIMAAircraft must not be empty.await
      dAIMAAircraft.map(_.head).map(_.numberRegistration) must beEqualTo("D-AIMA").await
      dAIMAAircraft.map(_.head).map(_.airplaneIataType) must beEqualTo("A380-800").await
      dAIMAAircraft.map(_.head).map(_.codeIataAirline) must beEqualTo("LH").await
      ok
    }
  }
}
