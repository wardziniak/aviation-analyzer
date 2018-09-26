package com.wardziniak.aviation.importer.external.converters

import com.wardziniak.aviation.api.model.{FlightSnapshot, Model}
import com.wardziniak.aviation.importer.external.model.{ExternalObject, FlightSnapshotDTO}

trait Converter[IN <: ExternalObject, OUT <: Model] {

  def convert(in: IN): OUT
}

object FlightSnapshotConverter extends Converter[FlightSnapshotDTO, FlightSnapshot] {
  import com.wardziniak.aviation.importer.external.model.FlightSnapshotDTO.asFlightSnapshot
  override def convert(in: FlightSnapshotDTO): FlightSnapshot = in
}
