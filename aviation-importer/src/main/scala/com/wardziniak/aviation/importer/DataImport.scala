package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.external.model.FlightSnapshot
import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.{BodyReadable, StandaloneWSClient, StandaloneWSRequest}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

//trait DataDownloader[ExternalFormat]{
//  def download(wsClient: StandaloneWSClient) = {
//    implicit val system = ActorSystem()
//    system.registerOnTermination {
//      System.exit(0)
//    }
//    implicit val materializer: ActorMaterializer = ActorMaterializer()
//
//    // Create the standalone WS client
//    // no argument defaults to a AhcWSClientConfig created from
//    // "AhcWSClientConfigFactory.forConfig(ConfigFactory.load, this.getClass.getClassLoader)"
//    //val wsClient = StandaloneAhcWSClient()
////    wsClient.url()
////
////    val res = Await.result(wsClient.url(url).get(), Duration.Inf)
////    val d: Future[StandaloneWSRequest#Response] = wsClient.url(url).get()
////    d.map(response => response.body[List[ExternalFormat]])
////    ???
//  }
//}

class DataImport[ExternalFormat] {


//  def download(wsClient: StandaloneWSClient, url: String)(implicit read: BodyReadable[ExternalFormat]) = {
//    wsClient.url(url).get().map(_.body[ExternalFormat])
//  }

  def loadData(wsClient: StandaloneWSClient, url: String)(d: String) = ???

  val url = ""
  implicit val system = ActorSystem()
  system.registerOnTermination {
    System.exit(0)
  }
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import com.wardziniak.aviation.importer.external.model.FlightSnapshot

//  val wsRequest = mock[WSRequest]
//  val wsResponse = mock[WSResponse]

  // Create the standalone WS client
  // no argument defaults to a AhcWSClientConfig created from
  // "AhcWSClientConfigFactory.forConfig(ConfigFactory.load, this.getClass.getClassLoader)"
  val wsClient = StandaloneAhcWSClient()
  val res = Await.result(wsClient.url(url).get(), Duration.Inf)

  println("sdasdassdsa")

  //def importData[ExternalFormat, InternalFormat]() = ???

}
