package com.wardziniak.aviation.app

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.api.{AirportDownloadAction, FlightDownloadAction}
import com.wardziniak.aviation.importer.config.ConfigLoader
import com.wardziniak.aviation.importer.flights.{FlightDataPublisherActor, FlightDownloaderActor}
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import com.wardziniak.aviation.importer.TopicName._

import scala.concurrent.duration._
import scala.language.postfixOps

object AirportDataImporterApp extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val system: ActorSystem = ActorSystem("aviation-edge-airports-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigLoader.loadConfig

  val publisher = system
    .actorOf(Props(FlightDataPublisherActor(kafkaServer = config.kafka.server, topic = airport)), name = "flightPublisher")
  val downloaderActor =
    system.actorOf(Props(FlightDownloaderActor(secretKey = config.serverKey, wsClient = StandaloneAhcWSClient())))

  val cancellable =
    system.scheduler.schedule(
      0 milliseconds,
      10 minute,
      downloaderActor,
      AirportDownloadAction)
}
