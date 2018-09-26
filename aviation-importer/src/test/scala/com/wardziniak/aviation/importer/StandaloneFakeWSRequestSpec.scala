package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.external.model.FlightSnapshotDTO
import org.f100ded.play.fakews._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import org.specs2.specification.core.Fragments
import play.api.libs.ws.{DefaultBodyReadables, DefaultBodyWritables}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source

class StandaloneFakeWSRequestSpec(implicit ee: ExecutionEnv)
  extends Specification
    with DefaultBodyWritables with DefaultBodyReadables {

  override def map(fragments: => Fragments): Fragments = fragments ^ step(afterAll())

  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()

  case class Person(name: String)

  "StandaloneFakeWSRequest" should {

    "simulate HTTP methods correctly" in {
      // implicit ee: ExecutionEnv =>


      //Source.fromResource("flights_data.json").mkString

      //val content = Source.fromResource("departure_data.json").mkString
      val content = Source.fromResource("flights_data.json").mkString

      val ws = StandaloneFakeWSClient {
        case GET(url"http://localhost/get") => Ok(content)
//        case GET(url"http://localhost/get") => Ok("[{\"name\": \"John\"}, {\"name\": \"James\"}, {\"name\": \"Jimmy\"}]")
        case POST(url"http://localhost/post") => Ok("post")
//        case PUT(url"http://localhost/put") => Ok("put")
//        case HEAD(url"http://localhost/head") => Ok("head")
//        case OPTIONS(url"http://localhost/options") => Ok("options")
//        case PATCH(url"http://localhost/patch") => Ok("patch")
//        case DELETE(url"http://localhost/delete") => Ok("delete")
      }

      import com.wardziniak.aviation.importer.external.model.FlightSnapshotDTO._

      val d = ws.url("http://localhost/get").get.map(
        response => response.body[List[FlightSnapshotDTO]]
      )




      val aa = Await.result(d, Duration.Inf)

      println("Test")

//      ws.url("http://localhost/get").get.map(_.body) must beEqualTo("get").await
      ws.url("http://localhost/post").post("").map(_.body) must beEqualTo("post").await
//      ws.url("http://localhost/put").put("").map(_.body) must beEqualTo("put").await
//      ws.url("http://localhost/head").head.map(_.body) must beEqualTo("head").await
//      ws.url("http://localhost/options").options.map(_.body) must beEqualTo("options").await
//      ws.url("http://localhost/patch").patch("").map(_.body) must beEqualTo("patch").await
//      ws.url("http://localhost/delete").delete.map(_.body) must beEqualTo("delete").await
    }

  }

  protected def afterAll(): Unit = {
    system.terminate()
  }
}