package com.wardziniak.aviation.importer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.external.model.FlightSnapshot
import play.api.libs.ws.{BodyReadable, StandaloneWSClient}

import scala.concurrent.{ExecutionContext, Future}


// Mamy jakiegoś url
// Ladujemy dane i deserializujemy zgodnie z modelem importu
// Konwertujemy do naszego formatu
// Wysyłamy do naszego storage'a

trait DataDownloader[ExternalFormat]{

  implicit val readableAsExternalFormat: BodyReadable[ExternalFormat]

  def download(wsClient: StandaloneWSClient, url: String)
    (implicit executor: ExecutionContext, materializer: ActorMaterializer): Future[ExternalFormat] = {

    wsClient.url(url).get.map(response => response.body[ExternalFormat])

  }
}


object FlightSnapshotDataDownloader extends DataDownloader[List[FlightSnapshot]] {
  import com.wardziniak.aviation.importer.external.model.FlightSnapshot.readableAsFlightSnapshot
  override implicit val readableAsExternalFormat: BodyReadable[List[FlightSnapshot]] = readableAsFlightSnapshot
}


