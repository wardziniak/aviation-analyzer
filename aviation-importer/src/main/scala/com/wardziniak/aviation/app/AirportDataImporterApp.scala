package com.wardziniak.aviation.app

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.TopicName._
import com.wardziniak.aviation.importer.api.AirportDownloadAction
import com.wardziniak.aviation.importer.config.ConfigLoader
import com.wardziniak.aviation.importer.airports._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object AirportDataImporterApp extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val system: ActorSystem = ActorSystem("aviation-edge-airports-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigLoader.loadConfig

  val publisher = system
    .actorOf(Props(AirportDataPublisherActor(kafkaServer = config.kafka.server, topic = airport)), name = "airportPublisher")
  val downloaderActor =
    system.actorOf(Props(AirportDownloaderActor(secretKey = config.serverKey, wsClient = StandaloneAhcWSClient())))

  system.scheduler.scheduleOnce(1 seconds, downloaderActor, AirportDownloadAction)

  Await.result(system.whenTerminated, 1 minutes)
}
