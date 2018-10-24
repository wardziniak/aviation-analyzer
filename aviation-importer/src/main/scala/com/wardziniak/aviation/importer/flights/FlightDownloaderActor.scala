package com.wardziniak.aviation.importer.flights

import akka.actor.ActorSelection
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.api.model.{FlightSnapshot, Value}
import com.wardziniak.aviation.importer.api.{DownloaderActor, FlightDownloadAction}
import play.api.libs.ws.{BodyReadable, StandaloneWSClient}

import scala.concurrent.ExecutionContext

case class FlightDownloaderActor(secretKey: String, wsClient: StandaloneWSClient)(implicit val materializer: ActorMaterializer, val executionContext: ExecutionContext)
  extends DownloaderActor[FlightSnapshotDTO, FlightSnapshot, FlightDownloadAction.type] {

  override val publisherActor: ActorSelection =
    context.system.actorSelection(s"/user/flightPublisher")
  import FlightSnapshotDTO.readableAsFlightSnapshot
  override implicit val readableAsExternalFormat: BodyReadable[List[FlightSnapshotDTO]] = readableAsFlightSnapshot

  override def transform(dto: FlightSnapshotDTO): Value = FlightSnapshotDTO.asFlightSnapshot(dto)
}