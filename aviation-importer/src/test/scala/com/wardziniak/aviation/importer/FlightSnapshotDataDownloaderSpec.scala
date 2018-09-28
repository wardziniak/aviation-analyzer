package com.wardziniak.aviation.importer
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.f100ded.play.fakews.{Ok, StandaloneFakeWSClient, _}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import play.api.libs.ws.DefaultBodyWritables

import scala.io.Source

class FlightSnapshotDataDownloaderSpec(implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables with FlightSnapshotDataDownloader {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val content: String = Source.fromResource("flights_data.json").mkString
  val wsClient = StandaloneFakeWSClient {
    case GET(url"http://localhost/get") => Ok(content)
  }

  "FlightSnapshotDataDownloader" should {

    "parse json properly" in {

      // test
      val data = download(url = "http://localhost/get")

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
