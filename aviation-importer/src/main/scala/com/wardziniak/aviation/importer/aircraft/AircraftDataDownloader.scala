package com.wardziniak.aviation.importer.aircraft

import com.wardziniak.aviation.importer.DataDownloader
import play.api.libs.ws.BodyReadable

trait AircraftDataDownloader extends DataDownloader[AircraftDTO] {
  import com.wardziniak.aviation.importer.aircraft.AircraftDTO.readableAsAircraftDTO
  override implicit val readableAsExternalFormat: BodyReadable[List[AircraftDTO]] = readableAsAircraftDTO
}
