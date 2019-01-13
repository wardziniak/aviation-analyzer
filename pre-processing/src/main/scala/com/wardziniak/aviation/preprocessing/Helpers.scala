package com.wardziniak.aviation.preprocessing

import com.wardziniak.aviation.api.model.{Airport, FlightSnapshot, InAirFlightData}

object Helpers {

  val EuropeTimezone = "Europe"

  // TODO implement way of calculating landing time based on distance, lastTimestamp, etc
  def calculateLandingTime(flightSnapshot: FlightSnapshot, airport: Airport): Long = {
    flightSnapshot.updated
  }

  def isClean: (String, FlightSnapshot) => Boolean = (_, flight) => {
    flight.flightNumber.iata.nonEmpty &&
      flight.departure.iata.nonEmpty &&
      flight.arrival.iata.nonEmpty &&
      flight.airlineCode.iata.nonEmpty
  }

  def allRecords: (String, FlightSnapshot) => Boolean = (_, _) => true

  def isEuropeAirport: (String, Airport) => Boolean = (_, airport) => airport.timezone.contains(EuropeTimezone)

  def isAirplaneLand(inAirFlightData: InAirFlightData, currentTimestamp: Long): Boolean = {
    val lastSnapshot = inAirFlightData.flightInfo.maxBy(_.updated)
    lastSnapshot.updated * SECONDS_TO_MS + LANDING_TIMEOUT < currentTimestamp &&
      lastSnapshot.localization.altitude < MINIMAL_LANDING_ALTITUDE
  }

  def isAirplaneLost(inAirFlightData: InAirFlightData, currentTimestamp: Long): Boolean = {
    val lastSnapshot = inAirFlightData.flightInfo.maxBy(_.updated)
    !isAirplaneLand(inAirFlightData, currentTimestamp) && lastSnapshot.updated * SECONDS_TO_MS + LOST_TIMEOUT < currentTimestamp
  }

}
