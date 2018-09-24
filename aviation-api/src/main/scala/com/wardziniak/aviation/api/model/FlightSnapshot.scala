package com.wardziniak.aviation.api.model

case class Localization(latitude: BigDecimal, longitude: BigDecimal, altitude: BigDecimal, direction: BigDecimal)

case class Speed(horizontal: BigDecimal, isGround: Boolean, vertical: BigDecimal)

case class AirportCode(iata: String, icao: String)

case class Aircraft(regNumber: String, icao: String, icao24: String, iata: String)

case class FlightNumber(iata: String, icao: String, number: String)

case class AirlineCode(iata: String, icao: String)

case class FlightSnapshot(
  localization: Localization,
  speed: Speed,
  departure: AirportCode,
  arrival: AirportCode,
  aircraft: Aircraft,
  flightNumber: FlightNumber,
  airlineCode: AirlineCode,
  enRoute: String, updated: Long)
