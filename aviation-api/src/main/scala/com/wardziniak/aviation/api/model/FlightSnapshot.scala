package com.wardziniak.aviation.api.model

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
  updated: Long) extends Value
