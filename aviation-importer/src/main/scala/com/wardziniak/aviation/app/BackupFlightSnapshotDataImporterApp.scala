package com.wardziniak.aviation.app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.wardziniak.aviation.importer.config.ConfigLoader
import com.wardziniak.aviation.importer.flights.FlightSnapshotDataImporter
import play.api.libs.ws.StandaloneWSClient
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object BackupFlightSnapshotDataImporterApp extends App {
  val config = ConfigLoader.loadConfig

  implicit val system: ActorSystem = ActorSystem("aviation-edge-actor-system")

  val dataImporter = new FlightSnapshotDataImporter {
    override def kafkaServer: String = config.kafka.server
    override implicit val executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    override implicit val materializer: ActorMaterializer = ActorMaterializer()
    override val wsClient: StandaloneWSClient = StandaloneAhcWSClient()
  }

  val flightUrl = "http://aviation-edge.com/v2/public/flights?key="

  val results = dataImporter.importData(url = s"$flightUrl${config.serverKey}")(topic = config.kafka.mainTopic)

  Await.result(results, Duration.Inf)
  dataImporter.close()
  val terminated = system.terminate()
  Await.result(terminated, Duration.Inf)
}
