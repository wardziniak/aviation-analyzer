package com.wardziniak.aviation.app

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.api.FlightDownloadAction
import com.wardziniak.aviation.importer.config.ConfigLoader
import com.wardziniak.aviation.importer.flights.{FlightDataPublisherActor, FlightDownloaderActor}
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import scala.concurrent.duration._
import scala.language.postfixOps

object FlightSnapshotDataImporterApp extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val system: ActorSystem = ActorSystem("aviation-edge-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigLoader.loadConfig

  val publisher = system
    .actorOf(Props(FlightDataPublisherActor(kafkaServer = config.kafka.server, topic = config.kafka.mainTopic)), name = "flightPublisher")
  val downloaderActor =
    system.actorOf(Props(FlightDownloaderActor(secretKey = config.serverKey, wsClient = StandaloneAhcWSClient())))

  val cancellable =
    system.scheduler.schedule(
      0 milliseconds,
      2 minute,
      downloaderActor,
      FlightDownloadAction)
}
