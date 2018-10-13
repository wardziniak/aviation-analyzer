package com.wardziniak.aviation.importer.aircraft

import com.wardziniak.aviation.api.model.Aircraft
import com.wardziniak.aviation.common.DefaultAircraftKafkaDataPublisher
import com.wardziniak.aviation.importer.DataImporter

import scala.language.implicitConversions

trait AircraftDataImporter
  extends DataImporter[AircraftDTO, Aircraft]
    with AircraftDataDownloader
    with DefaultAircraftKafkaDataPublisher {
  override implicit def asValue(dto: AircraftDTO): Aircraft = AircraftDTO.asAircraft(dto)

  def close(): Unit = {
    wsClient.close()
    materializer.shutdown()
  }
}
