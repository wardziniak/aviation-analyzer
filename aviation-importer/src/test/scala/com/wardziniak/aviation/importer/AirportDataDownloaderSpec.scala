package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.importer.airports.AirportDataDownloader
import com.wardziniak.aviation.importer.flights.FlightSnapshotDataDownloader
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import play.api.libs.ws.DefaultBodyWritables

import scala.io.Source
import scala.language.reflectiveCalls
import scala.io.Source

class AirportDataDownloaderSpec (implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables
    with AirportDataDownloader
    with LazyLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val fileName = s"file:///${this.getClass.getResource("/airports.json").getPath}"
  val content: String = Source.fromURL(fileName).mkString

  //  val content: String = Source.fromResource("flights_data.json").mkString
  val flightUrl: String = "http://aviation-edge.com/v2/public/airportDatabase?key=someKey"
  val wsClient = StandaloneFakeWSClient {
    case GET(url"$flightUrl") => Ok(content)
  }

  "FlightSnapshotDataDownloader" should {

    "parse json properly" in {

      logger.trace("trace")
      logger.debug("debug")
      logger.info("info")
      logger.error("error")

      // test
      val data = download(url = flightUrl)

      // validation
      val airportModlin = data.map(t => t.find(_.codeIataAirport == "WMI"))
      airportModlin.map(_.head).map(_.codeIcaoAirport) must beEqualTo("EPMO").await
      airportModlin.map(_.head).map(_.codeIataCity) must beEqualTo("WMI").await
      airportModlin.map(_.head).map(_.nameAirport) must beEqualTo("Modlin").await
      airportModlin.map(_.head).map(_.nameCountry) must beEqualTo("Poland").await
      airportModlin.map(_.head).map(_.codeIso2Country) must beEqualTo("PL").await
    }
  }


}