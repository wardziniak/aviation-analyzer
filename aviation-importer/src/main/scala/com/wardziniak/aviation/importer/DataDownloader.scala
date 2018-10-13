package com.wardziniak.aviation.importer

import akka.stream.ActorMaterializer
import play.api.libs.ws.{BodyReadable, StandaloneWSClient}

import scala.concurrent.{ExecutionContext, Future}

trait DataDownloader[ExternalFormat]{

  implicit val readableAsExternalFormat: BodyReadable[List[ExternalFormat]]

  val wsClient: StandaloneWSClient

  def download(url: String)
    (implicit executor: ExecutionContext, materializer: ActorMaterializer): Future[List[ExternalFormat]] = {
    wsClient.url(url).get.map(response => response.body[List[ExternalFormat]])
  }
}

