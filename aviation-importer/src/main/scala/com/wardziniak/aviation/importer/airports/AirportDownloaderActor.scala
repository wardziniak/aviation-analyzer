package com.wardziniak.aviation.importer.airports

import akka.actor.ActorSelection
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.api.model.{Airport, Value}
import com.wardziniak.aviation.importer.api.{AirportDownloadAction, DownloaderActor}
import play.api.libs.ws.{BodyReadable, StandaloneWSClient}

import scala.concurrent.ExecutionContext

case class AirportDownloaderActor (secretKey: String, wsClient: StandaloneWSClient)(implicit val materializer: ActorMaterializer, val executionContext: ExecutionContext)
  extends DownloaderActor[AirportDTO, Airport, AirportDownloadAction.type] {

  override val publisherActor: ActorSelection =
    context.system.actorSelection(s"/user/airportPublisher")
  import AirportDTO.readableAsAirport
  override implicit val readableAsExternalFormat: BodyReadable[List[AirportDTO]] = readableAsAirport

  override def transform(dto: AirportDTO): Value = AirportDTO.asAirport(dto)

  override def test(in: AirportDTO): Boolean = in.codeIata.nonEmpty
}
