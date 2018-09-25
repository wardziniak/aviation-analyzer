package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.shaded.ahc.org.asynchttpclient.{Request, RequestBuilderBase, SignatureCalculator}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.libs.ws.{DefaultBodyWritables, StandaloneWSRequest, StandaloneWSResponse}
import play.libs.ws.DefaultBodyReadables
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.f100ded.play.fakews._
import org.scalatest._
import org.specs2.specification.core.Fragments
import play.api.libs.json.Json
//import play.api.libs.ws.JsonBodyWritables._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable._
import org.specs2.specification.core.Fragments
import play.api.libs.ws.DefaultBodyWritables

class TmpImporter extends Specification with DefaultBodyWritables{


//    val url = ""
//    implicit val system = ActorSystem()
//    system.registerOnTermination {
//      System.exit(0)
//    }
//    implicit val materializer: ActorMaterializer = ActorMaterializer()

    //override def map(fragments: => Fragments): Fragments = fragments ^ step(afterAll())

//    implicit val system = ActorSystem()
//
//    implicit val mat = ActorMaterializer()
//
//    "simulate HTTP methods correctly" in { implicit ee: ExecutionEnv =>
//        val ws = StandaloneFakeWSClient {
//            case GET(url"http://localhost/get") => Ok("get")
//            case POST(url"http://localhost/post") => Ok("post")
//            case PUT(url"http://localhost/put") => Ok("put")
//            case HEAD(url"http://localhost/head") => Ok("head")
//            case OPTIONS(url"http://localhost/options") => Ok("options")
//            case PATCH(url"http://localhost/patch") => Ok("patch")
//            case DELETE(url"http://localhost/delete") => Ok("delete")
//        }
//
//        ws.url("http://localhost/get").get.map(_.body) must beEqualTo("get").await
//        ws.url("http://localhost/post").post("").map(_.body) must beEqualTo("post").await
//        ws.url("http://localhost/put").put("").map(_.body) must beEqualTo("put").await
//        ws.url("http://localhost/head").head.map(_.body) must beEqualTo("head").await
//        ws.url("http://localhost/options").options.map(_.body) must beEqualTo("options").await
//        ws.url("http://localhost/patch").patch("").map(_.body) must beEqualTo("patch").await
//        ws.url("http://localhost/delete").delete.map(_.body) must beEqualTo("delete").await
//    }
//
//    // Create the standalone WS client
//    // no argument defaults to a AhcWSClientConfig created from
//    // "AhcWSClientConfigFactory.forConfig(ConfigFactory.load, this.getClass.getClassLoader)"
////    val wsClient = StandaloneAhcWSClient()
////    val res = Await.result(wsClient.url(url).get(), Duration.Inf)
//
//    println("sdasdassdsa")

    //def importData[ExternalFormat, InternalFormat]() = ???

}
