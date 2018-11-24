package com.wardziniak.aviation.api.model

import scala.language.implicitConversions

case class Localization(latitude: Double, longitude: Double, altitude: Double, direction: Double)

case class Speed(horizontal: Double, isGround: Boolean, vertical: Double)

case class AirportCode(iata: String, icao: String)

case class AircraftCode(regNumber: String, icao: String, icao24: String, iata: String)

case class FlightNumber(iata: String, icao: String, number: String)

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
  updated: Long,
  landedTimestamp: Option[Long] = None) extends Value {

  def withLandingData(arrivalAirport: Airport, landedTimestamp: Long): AnalyticFlightSnapshot = {
    ???
  }

  def withAirportData(arrivalAirport: Airport): AnalyticFlightSnapshot = {
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
      landedTimestamp = this.landedTimestamp,
      arrivalAirport = arrivalAirport
    )
  }

//  implicit def asAnalyticFlightSnapshot(flightSnapshot: FlightSnapshot): AnalyticFlightSnapshot = {
//    AnalyticFlightSnapshot(
//      localization = flightSnapshot.localization,
//      speed = flightSnapshot.speed,
//      departure = flightSnapshot.departure,
//      arrival = flightSnapshot.arrival,
//      aircraft = flightSnapshot.aircraft,
//      flightNumber = flightSnapshot.flightNumber,
//      airlineCode = flightSnapshot.airlineCode,
//      enRoute = flightSnapshot.enRoute,
//      updated = flightSnapshot.updated)
//  }

  def witLandedTimestamp(landedTimestamp: Long): FlightSnapshot = this.copy(landedTimestamp = Some(landedTimestamp))

//  def prepareAnalyticFlightSnapshot(landedTimestamp: Long, latitude: Double, longitude: Double): AnalyticFlightSnapshot = {
//    this.withLandedTime(landedTimestamp).withAirportGeoLocation(latitude, longitude)
//  }
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
  landedTimestamp: Option[Long] = None,
  arrivalAirport: Airport
) extends Value {

//  def withLandedTime(landedTimestamp: Long): AnalyticFlightSnapshot = {
//    this.copy(landedTimestamp = Some(landedTimestamp), timeToTarget = Some(landedTimestamp - updated))
//  }

  def withAirportGeoLocation(latitude: Double, longitude: Double): AnalyticFlightSnapshot = {
    this
  }

}
