package com.wardziniak.aviation.importer

import akka.stream.ActorMaterializer
import com.wardziniak.aviation.api.model.{FlightSnapshot, Value}
import com.wardziniak.aviation.importer.external.{FlightSnapshotKafkaDataPublisher, KafkaDataPublisher}
import com.wardziniak.aviation.importer.external.model.{ExternalObject, FlightSnapshotDTO}
import org.apache.kafka.clients.producer.KafkaProducer

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions

trait DataImporter[ExternalFormat <: ExternalObject,V <: Value] {
  self: DataDownloader[ExternalFormat] with KafkaDataPublisher[String, V]=>

  implicit def asValue(dto: ExternalFormat): V

  implicit val executor: ExecutionContext

  implicit val materializer: ActorMaterializer
  //val publisher: BasicKafkaDataPublisher[V]



  implicit def asValueList(dtos: List[ExternalFormat]): List[V] = dtos.map(asValue)


  def importData(url: String)(producer: KafkaProducer[String, V], topic: String) = {
    download(url).flatMap(rawRecords => publish(topic, rawRecords))
  }

}

trait FlightSnapshotDataImporter
  extends DataImporter[FlightSnapshotDTO, FlightSnapshot]
    with FlightSnapshotDataDownloader
    with FlightSnapshotKafkaDataPublisher {
  override implicit def asValue(dto: FlightSnapshotDTO): FlightSnapshot = FlightSnapshotDTO.asFlightSnapshot(dto)
}
