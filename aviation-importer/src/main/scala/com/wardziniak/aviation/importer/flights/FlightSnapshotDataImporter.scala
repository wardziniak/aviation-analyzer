package com.wardziniak.aviation.importer.flights

import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.DefaultFlightSnapshotKafkaDataPublisher
import com.wardziniak.aviation.importer.DataImporter

import scala.language.implicitConversions

trait FlightSnapshotDataImporter
  extends DataImporter[FlightSnapshotDTO, FlightSnapshot]
    with FlightSnapshotDataDownloader
    with DefaultFlightSnapshotKafkaDataPublisher {
  override implicit def asValue(dto: FlightSnapshotDTO): FlightSnapshot = FlightSnapshotDTO.asFlightSnapshot(dto)

  def close(): Unit = {
    wsClient.close()
    materializer.shutdown()
  }
}
