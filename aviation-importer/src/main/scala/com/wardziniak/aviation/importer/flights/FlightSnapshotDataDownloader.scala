package com.wardziniak.aviation.importer.flights

import com.wardziniak.aviation.importer.DataDownloader
import play.api.libs.ws.BodyReadable

trait FlightSnapshotDataDownloader extends DataDownloader[FlightSnapshotDTO] {
  import FlightSnapshotDTO.readableAsFlightSnapshot
  override implicit val readableAsExternalFormat: BodyReadable[List[FlightSnapshotDTO]] = readableAsFlightSnapshot
}

