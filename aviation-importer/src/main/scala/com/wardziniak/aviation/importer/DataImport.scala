package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.external.model.FlightSnapshot
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait DataDownloader[ExternalFormat]{
  def download(url: String) = {
    implicit val system = ActorSystem()
    system.registerOnTermination {
      System.exit(0)
    }
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    // Create the standalone WS client
    // no argument defaults to a AhcWSClientConfig created from
    // "AhcWSClientConfigFactory.forConfig(ConfigFactory.load, this.getClass.getClassLoader)"
    val wsClient = StandaloneAhcWSClient()
    val res = Await.result(wsClient.url(url).get(), Duration.Inf)
//    val d: Future[StandaloneWSRequest#Response] = wsClient.url(url).get()
//    d.map(response => response.body[List[ExternalFormat]])
//    ???
  }
}

object DataImport {

  val url = ""
  implicit val system = ActorSystem()
  system.registerOnTermination {
    System.exit(0)
  }
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val FlightSnapshotFormat: OFormat[FlightSnapshot] = Json.format[FlightSnapshot]

  // Create the standalone WS client
  // no argument defaults to a AhcWSClientConfig created from
  // "AhcWSClientConfigFactory.forConfig(ConfigFactory.load, this.getClass.getClassLoader)"
  val wsClient = StandaloneAhcWSClient()
  val res = Await.result(wsClient.url(url).get(), Duration.Inf)

  println("sdasdassdsa")

  //def importData[ExternalFormat, InternalFormat]() = ???

}
