package com.wardziniak.aviation.importer.api

import akka.actor.{Actor, ActorSelection}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.Value
import com.wardziniak.aviation.importer.external.model.DTO
import play.api.libs.ws.{BodyReadable, StandaloneWSClient}

import scala.concurrent.{ExecutionContext, Future}

/**
  *
  * @tparam IN - type of downloading objects
  *
  *           When receive DownloadAction message start downloading and sending data to @publisherActor,
  *           that is responsible for putting data in Kafka
  */


trait DownloaderActor[IN <: DTO, OUT <: Value, ActionType <: DownloadAction] extends Actor
    with LazyLogging {

  import DownloaderActor._

  // Passed in constructor
  val secretKey: String

  // will be retrieved based on Actor type
  val publisherActor: ActorSelection

  // Bedą implicity w case class
  implicit val executionContext: ExecutionContext
  implicit val materializer: ActorMaterializer

  implicit val readableAsExternalFormat: BodyReadable[List[IN]]
  val wsClient: StandaloneWSClient

  def transform(dto: IN): Value


  def download(url: String): Future[List[IN]] = {
    wsClient.url(url).get.map(response => response.body[List[IN]])
  }

  override def receive = {
    case action: ActionType =>
      val url = s"$BaseUrl${action.endpoint}?key=$secretKey${action.queryParameters}"
      download(url).map(rawRecords =>
        rawRecords.foreach(location => {
          logger.trace(s"$location")
          publisherActor ! transform(location)
        }
        ))
  }
}

object DownloaderActor {
  val BaseUrl = "https://aviation-edge.com/v2/public/"
}
