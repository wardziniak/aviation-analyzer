package com.wardziniak.aviation.importer.airports

import com.wardziniak.aviation.api.model.Airport
import com.wardziniak.aviation.common.DefaultAirportKafkaDataPublisher
import com.wardziniak.aviation.importer.DataImporter

import scala.language.implicitConversions

trait AirportDataImporter
  extends DataImporter[AirportDTO, Airport]
    with AirportDataDownloader
    with DefaultAirportKafkaDataPublisher {

  override implicit def asValue(dto: AirportDTO): Airport = AirportDTO.asAirport(dto)

  def close(): Unit = {
    wsClient.close()
    materializer.shutdown()
  }
}
