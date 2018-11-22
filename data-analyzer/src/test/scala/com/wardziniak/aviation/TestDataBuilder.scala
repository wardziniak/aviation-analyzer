package com.wardziniak.aviation

import com.wardziniak.aviation.api.model._

object TestDataBuilder {

  val flightSnapshot: FlightSnapshot = FlightSnapshot(
    localization = Localization(latitude = 1.1, longitude = 1.1, altitude = 1.2, direction = 2.3),
    speed = Speed(horizontal = 1.1, isGround = false, vertical = 1.3),
    departure = AirportCode(iata = "LHR", icao = "EGLL"),
    arrival = AirportCode(iata = "FRA", icao = "EDDF"),
    aircraft = AircraftCode(regNumber = "DCAL", icao = "DCA11", icao24 = "DCA324", iata = "DCA123"),
    flightNumber =  FlightNumber(iata = "LOT233", icao = "LO233", number = "233"),
    airlineCode = AirlineCode(iata = "LH", icao = "DLH"),
    enRoute =  "enRoute",
    updated = 1233
  )

  val dirtyFlightSnapshot: FlightSnapshot = FlightSnapshot(
    localization = Localization(latitude = 1.1, longitude = 1.1, altitude = 1.2, direction = 2.3),
    speed = Speed(horizontal = 1.1, isGround = false, vertical = 1.3),
    departure = AirportCode(iata = "", icao = "EGLL"),
    arrival = AirportCode(iata = "FRA", icao = "EDDF"),
    aircraft = AircraftCode(regNumber = "DCAL", icao = "DCA11", icao24 = "DCA324", iata = "DCA123"),
    flightNumber =  FlightNumber(iata = "LOT233", icao = "LO233", number = "233"),
    airlineCode = AirlineCode(iata = "LH", icao = "DLH"),
    enRoute =  "enRoute",
    updated = 1233
  )
}
