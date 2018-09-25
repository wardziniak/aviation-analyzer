package com.wardziniak.aviation.importer.external.model

import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.BodyReadable

case class Geography(latitude: BigDecimal, longitude: BigDecimal, altitude: BigDecimal, direction: BigDecimal)

case class Speed(horizontal: BigDecimal, isGround: Int, vertical: BigDecimal)

case class AirportCode(iataCode: String, icaoCode: String)

case class Aircraft(regNumber: String, icaoCode: String, icao24: String, iataCode: String)

case class FlightNumber(iataNumber: String, icaoNumber: String, number: String)

case class System(updated: String, squawk: String)

case class AirlineCode(iataCode: String, icaoCode: String)

case class FlightSnapshot(
  geography: Geography,
  speed: Speed,
  departure: AirportCode,
  arrival: AirportCode,
  aircraft: Aircraft,
  flight: FlightNumber,
  airline: AirlineCode,
  system: System,
  status: String)

object FlightSnapshot {
  implicit val GeographyFormat: OFormat[Geography] = Json.format[Geography]
  implicit val SpeedFormat: OFormat[Speed] = Json.format[Speed]
  implicit val AirportCodeFormat: OFormat[AirportCode] = Json.format[AirportCode]
  implicit val AircraftCodeFormat: OFormat[Aircraft] = Json.format[Aircraft]
  implicit val FlightNumberFormat: OFormat[FlightNumber] = Json.format[FlightNumber]
  implicit val SystemFormat: OFormat[System] = Json.format[System]
  implicit val AirlineCodeFormat: OFormat[AirlineCode] = Json.format[AirlineCode]
  implicit val FlightSnapshotFormat: OFormat[FlightSnapshot] = Json.format[FlightSnapshot]

  implicit val readableAsFlightSnapshot: BodyReadable[List[FlightSnapshot]] = BodyReadable { response =>
    Json.fromJson[List[FlightSnapshot]](Json.parse(response.bodyAsBytes.toArray)).get
  }
}
