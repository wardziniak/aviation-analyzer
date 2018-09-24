package com.wardziniak.aviation.importer.external.model

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
  airlineCode: AirlineCode,
  system: System,
  status: String)
