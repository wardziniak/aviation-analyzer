package com.wardziniak.aviation.importer
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.importer.flights.FlightSnapshotDataDownloader
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import play.api.libs.ws.DefaultBodyWritables

import scala.io.Source
import scala.language.reflectiveCalls

class FlightSnapshotDataDownloaderSpec(implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables with FlightSnapshotDataDownloader
    with LazyLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val fileName = s"file:///${this.getClass.getResource("/flights_data.json").getPath}"
  val content: String = Source.fromURL(fileName).mkString

//  val content: String = Source.fromResource("flights_data.json").mkString
  val flightUrl: String = "http://aviation-edge.com/v2/public/flights?key=someKey"
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
      val splndFlitgh = data.map(t => t.find(_.aircraft.regNumber == "SPLND"))
      data.map(_.size) must beEqualTo(4).await
      splndFlitgh must not be empty.await
      splndFlitgh.map(_.head).map(_.geography.latitude) must beEqualTo(BigDecimal(42.6827)).await
      splndFlitgh.map(_.head).map(_.speed.horizontal) must beEqualTo(BigDecimal(746.46)).await
      splndFlitgh.map(_.head).map(_.departure.iataCode) must beEqualTo("WAW").await
      splndFlitgh.map(_.head).map(_.arrival.iataCode) must beEqualTo("IST").await
      splndFlitgh.map(_.head).map(_.flight.icaoNumber) must beEqualTo("LOT135").await

    }
  }


}
