package com.wardziniak.aviation.api.model

import scala.language.implicitConversions

case class Localization(latitude: Double, longitude: Double, altitude: Double, direction: Double) {
  def getNthLocation(size: Int, number: Int, destination: Localization) =
    this.copy(
      latitude = latitude + (destination.latitude - latitude) / size * number,
      longitude = longitude + (destination.longitude - longitude) / size * number,
      altitude = altitude + (destination.altitude - altitude) / size * number,
      direction = direction + (destination.direction - direction) / size * number
    )
}

case class Speed(horizontal: Double, isGround: Boolean, vertical: Double) {
  def getNthLocation(size: Int, number: Int, destination: Speed) =
    this.copy(
      horizontal = horizontal + (destination.horizontal - horizontal) / size * number,
      isGround = destination.isGround,
      vertical = vertical + (destination.vertical - vertical) / size * number
    )
}

case class AirportCode(iata: String, icao: String)

case class AircraftCode(regNumber: String, icao: String, icao24: String, iata: String)

case class FlightNumber(iata: String, icao: String, number: String)

object FlightSnapshot {
  type FlightNumberIata = String
  type FlightNumberIcao = String
}

case class AirlineCode(iata: String, icao: String)

case class FlightSnapshot(
  localization: Localization,
  speed: Speed,
  departure: AirportCode,
  arrival: AirportCode,
  aircraft: AircraftCode,
  flightNumber: FlightNumber,
  airlineCode: AirlineCode,
  enRoute: String,
  updated: Long) extends Value {

  def withLandingData(arrivalAirport: Airport, landedTimestamp: Long): AnalyticFlightSnapshot = {
    AnalyticFlightSnapshot(
      localization = this.localization,
      speed = this.speed,
      departure = this.departure,
      arrival = this.arrival,
      aircraft = this.aircraft,
      flightNumber = this.flightNumber,
      airlineCode = this.airlineCode,
      enRoute = this.enRoute,
      updated = this.updated,
      landedTimestamp = landedTimestamp,
      arrivalAirport = arrivalAirport,
      distance = ModelUtils.calculateDistance(
        fromLatitude = localization.latitude,
        fromLongitude = localization.longitude,
        toLatitude = arrivalAirport.latitude,
        toLongitude = arrivalAirport.longitude,
        airplaneAltitude = localization.altitude
      )
    )
  }

//  def withAirportData(arrivalAirport: Airport): AnalyticFlightSnapshot = {
//    AnalyticFlightSnapshot(
//      localization = this.localization,
//      speed = this.speed,
//      departure = this.departure,
//      arrival = this.arrival,
//      aircraft = this.aircraft,
//      flightNumber = this.flightNumber,
//      airlineCode = this.airlineCode,
//      enRoute = this.enRoute,
//      updated = this.updated,
//      landedTimestamp = this.landedTimestamp.get,
//      arrivalAirport = arrivalAirport
//    )
//  }

//  def witLandedTimestamp(landedTimestamp: Long): FlightSnapshot = this.copy(landedTimestamp = Some(landedTimestamp))
}

case class AnalyticFlightSnapshot(
  localization: Localization,
  speed: Speed,
  departure: AirportCode,
  arrival: AirportCode,
  aircraft: AircraftCode,
  flightNumber: FlightNumber,
  airlineCode: AirlineCode,
  enRoute: String,
  updated: Long,
  landedTimestamp: Long,
  arrivalAirport: Airport,
  distance: Double
) extends Value {


  def withAirportGeoLocation(latitude: Double, longitude: Double): AnalyticFlightSnapshot = {
    this
  }
}

case class TrainFlightSnapshot(
  localization: Localization,
  speed: Speed,
  departure: AirportCode,
  arrival: AirportCode,
  aircraft: AircraftCode,
  flightNumber: FlightNumber,
  airlineCode: AirlineCode,
  enRoute: String,
  updated: Long,
  landedTimestamp: Long,
  arrivalAirport: Airport,
)
