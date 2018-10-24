package com.wardziniak.aviation.importer.flights

import com.wardziniak.aviation.api.model._
import com.wardziniak.aviation.importer.external.model.DTO
import play.api.libs.json.{Json, OFormat}
import play.api.libs.ws.BodyReadable

import scala.language.implicitConversions

case class GeographyDTO(latitude: Double, longitude: Double, altitude: Double, direction: Double) extends DTO

case class SpeedDTO(horizontal: Double, isGround: Int, vertical: Double) extends DTO

case class AirportCodeDTO(iataCode: String, icaoCode: String) extends DTO

case class AircraftBaseDTO(regNumber: String, icaoCode: String, icao24: String, iataCode: String) extends DTO

case class FlightNumberDTO(iataNumber: String, icaoNumber: String, number: String) extends DTO

case class SystemDTO(updated: String, squawk: String) extends DTO

case class AirlineCodeDTO(iataCode: String, icaoCode: String)

case class FlightSnapshotDTO(
  geography: GeographyDTO,
  speed: SpeedDTO,
  departure: AirportCodeDTO,
  arrival: AirportCodeDTO,
  aircraft: AircraftBaseDTO,
  flight: FlightNumberDTO,
  airline: AirlineCodeDTO,
  system: SystemDTO,
  status: String) extends DTO

object FlightSnapshotDTO {
  implicit val GeographyFormat: OFormat[GeographyDTO] = Json.format[GeographyDTO]
  implicit val SpeedFormat: OFormat[SpeedDTO] = Json.format[SpeedDTO]
  implicit val AirportCodeFormat: OFormat[AirportCodeDTO] = Json.format[AirportCodeDTO]
  implicit val AircraftCodeFormat: OFormat[AircraftBaseDTO] = Json.format[AircraftBaseDTO]
  implicit val FlightNumberFormat: OFormat[FlightNumberDTO] = Json.format[FlightNumberDTO]
  implicit val SystemFormat: OFormat[SystemDTO] = Json.format[SystemDTO]
  implicit val AirlineCodeFormat: OFormat[AirlineCodeDTO] = Json.format[AirlineCodeDTO]
  implicit val FlightSnapshotFormat: OFormat[FlightSnapshotDTO] = Json.format[FlightSnapshotDTO]

  implicit val readableAsFlightSnapshot: BodyReadable[List[FlightSnapshotDTO]] = BodyReadable { response =>
    Json.fromJson[List[FlightSnapshotDTO]](Json.parse(response.bodyAsBytes.toArray)).get
  }

  implicit def asGeography(dto: GeographyDTO): Localization = Localization(
    latitude = dto.latitude,
    longitude = dto.longitude,
    altitude = dto.altitude,
    direction = dto.direction)

  implicit def asSpeed(dto: SpeedDTO): Speed = Speed(
    horizontal = dto.horizontal,
    vertical = dto.vertical,
    isGround = dto.isGround != 0)

  implicit def asAirportCode(dto: AirportCodeDTO): AirportCode = AirportCode(
    iata = dto.iataCode,
    icao = dto.icaoCode)

  implicit def asAircraft(dto: AircraftBaseDTO): AircraftCode = AircraftCode(
    regNumber = dto.regNumber,
    icao = dto.icaoCode,
    icao24 = dto.icao24,
    iata = dto.iataCode)

  implicit def asFlightNumber(dto: FlightNumberDTO): FlightNumber = FlightNumber(
    iata = dto.iataNumber,
    icao = dto.icaoNumber,
    number = dto.number)

  implicit def asAirlineCode(dto: AirlineCodeDTO): AirlineCode = AirlineCode(
    iata = dto.iataCode,
    icao = dto.icaoCode)

  implicit def asFlightSnapshot(dto: FlightSnapshotDTO): FlightSnapshot = FlightSnapshot(
    localization = dto.geography,
    speed = dto.speed,
    departure = dto.departure,
    arrival = dto.arrival,
    aircraft = dto.aircraft,
    flightNumber = dto.flight,
    airlineCode = dto.airline,
    enRoute = dto.status,
    updated = dto.system.updated.toLong)

}
