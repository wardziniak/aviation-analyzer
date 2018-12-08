package com.wardziniak.aviation.importer.airports

import com.wardziniak.aviation.importer.DataDownloader
import play.api.libs.ws.BodyReadable

trait AirportDataDownloader extends DataDownloader[AirportDTO] {
  import AirportDTO.readableAsAirport
  override implicit val readableAsExternalFormat: BodyReadable[List[AirportDTO]] = readableAsAirport
}
