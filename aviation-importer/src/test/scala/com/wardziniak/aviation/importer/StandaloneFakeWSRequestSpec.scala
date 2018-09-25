package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.external.model.{Aircraft, AirlineCode, AirportCode, FlightNumber, FlightSnapshot, Geography, Speed, System}
import org.f100ded.play.fakews._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.specification.core.Fragments
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ahc.{StandaloneAhcWSClient, StandaloneAhcWSResponse}
import play.api.libs.ws.{BodyReadable, DefaultBodyReadables, DefaultBodyWritables, StandaloneWSClient}

import scala.concurrent.Future
import scala.io.Source
//import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Await
import scala.concurrent.duration.Duration

class StandaloneFakeWSRequestSpec(implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables with DefaultBodyReadables {

  override def map(fragments: => Fragments): Fragments = fragments ^ step(afterAll())

  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()

  case class Person(name: String)

  "StandaloneFakeWSRequest" should {

    "simulate HTTP methods correctly" in {// implicit ee: ExecutionEnv =>


      //Source.fromResource("flights_data.json").mkString

      //val content = Source.fromResource("departure_data.json").mkString
      val content = Source.fromResource("flights_data.json").mkString

      val ws = StandaloneFakeWSClient {
        case GET(url"http://localhost/get") => Ok(content)
//        case GET(url"http://localhost/get") => Ok("[{\"name\": \"John\"}, {\"name\": \"James\"}, {\"name\": \"Jimmy\"}]")
//        case POST(url"http://localhost/post") => Ok("post")
//        case PUT(url"http://localhost/put") => Ok("put")
//        case HEAD(url"http://localhost/head") => Ok("head")
//        case OPTIONS(url"http://localhost/options") => Ok("options")
//        case PATCH(url"http://localhost/patch") => Ok("patch")
//        case DELETE(url"http://localhost/delete") => Ok("delete")
      }

      import play.api.libs.ws.JsonBodyReadables._
      import play.api.libs.ws.JsonBodyWritables._
      import play.api.libs.json._
      import play.api.libs.json.Json
//      implicit val userJsonFormat = Json.format[Person]
//
//      implicit val readableAsJson: BodyReadable[List[Person]] = BodyReadable { response =>
//        Json.fromJson[List[Person]](Json.parse(response.bodyAsBytes.toArray)).get
//      }
//
//
//      implicit val personReads: Reads[Person] = Json.reads[Person]
//      val d = ws.url("http://localhost/get").get.map(
//        response => response.body[List[Person]]
//      )

//      implicit val geographyReads: Reads[Geography] = Json.reads[Geography]
//      implicit val speedReads: Reads[Speed] = Json.reads[Speed]
//      implicit val airportCodeReads: Reads[AirportCode] = Json.reads[AirportCode]
//      implicit val aircraftCodeReads: Reads[Aircraft] = Json.reads[Aircraft]
//      implicit val flightNumberReads: Reads[FlightNumber] = Json.reads[FlightNumber]
//      implicit val systemReads: Reads[System] = Json.reads[System]
//      implicit val airlineCodeReads: Reads[AirlineCode] = Json.reads[AirlineCode]
//      implicit val flightSnapshotReads: Reads[FlightSnapshot] = Json.reads[FlightSnapshot]
//
//      implicit val readableAsFlightSnapshot: BodyReadable[List[FlightSnapshot]] = BodyReadable { response =>
//        Json.fromJson[List[FlightSnapshot]](Json.parse(response.bodyAsBytes.toArray)).get
//      }

      import com.wardziniak.aviation.importer.external.model.FlightSnapshot._

      val d = ws.url("http://localhost/get").get.map(
        response => response.body[List[FlightSnapshot]]
      )

      val aa = Await.result(d, Duration.Inf)

      println("Test")

      ws.url("http://localhost/get").get.map(_.body) must beEqualTo("get").await
      ws.url("http://localhost/post").post("").map(_.body) must beEqualTo("post").await
      ws.url("http://localhost/put").put("").map(_.body) must beEqualTo("put").await
      ws.url("http://localhost/head").head.map(_.body) must beEqualTo("head").await
      ws.url("http://localhost/options").options.map(_.body) must beEqualTo("options").await
      ws.url("http://localhost/patch").patch("").map(_.body) must beEqualTo("patch").await
      ws.url("http://localhost/delete").delete.map(_.body) must beEqualTo("delete").await
    }

//    "not add Content-Type if body is empty" in {
//      val ws = StandaloneFakeWSClient(Ok)
//      val r = ws.url("http://localhost/").asInstanceOf[StandaloneFakeWSRequest]
//      r.fakeRequest.headers.get("Content-Type") must beNone
//    }
//
//    "set Content-Type to text/plain if body is text" in {
//      val ws = StandaloneFakeWSClient(Ok)
//      val r = ws.url("http://localhost/")
//        .withBody("hello world")
//        .asInstanceOf[StandaloneFakeWSRequest]
//
//      r.contentType must beSome("text/plain")
//    }
//
//    "not change explicitly set Content-Type" in {
//      val ws = StandaloneFakeWSClient(Ok)
//      val r = ws.url("http://localhost/")
//        .withHttpHeaders("Content-Type" -> "text/special")
//        .withBody("hello world")
//        .asInstanceOf[StandaloneFakeWSRequest]
//      r.fakeRequest.headers.get("Content-Type") must beSome(Seq("text/special"))
//    }
//
//    "add query string params" in {
//      val ws = StandaloneFakeWSClient(Ok)
//      val r = ws.url("http://localhost/")
//        .withQueryStringParameters(
//          "a" -> "1",
//          "b" -> "2"
//        ).asInstanceOf[StandaloneFakeWSRequest]
//      r.fakeRequest.url must contain("a=1")
//      r.fakeRequest.url must contain("b=2")
//    }
  }

  protected def afterAll(): Unit = {
    system.terminate()
  }
}