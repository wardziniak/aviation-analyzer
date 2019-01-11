package com.wardziniak.aviation.preprocessing

import com.wardziniak.aviation.api.model.{Airport, FlightSnapshot}

object Helpers {

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

}
