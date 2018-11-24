package com.wardziniak.aviation.preprocessing

import com.wardziniak.aviation.api.model.{Airport, FlightSnapshot}

object Helpers {

  // TODO implement way of calculating landing time based on distance, lastTimestamp, etc
  def calculateLandingTime(value: (FlightSnapshot, Airport)): FlightSnapshot = {
    value._1.copy(landedTimestamp = Some(value._1.updated))
  }

  def isClean: (String, FlightSnapshot) => Boolean = (_, flight) => {
    flight.flightNumber.iata.nonEmpty &&
      flight.departure.iata.nonEmpty &&
      flight.arrival.iata.nonEmpty &&
      flight.airlineCode.iata.nonEmpty
  }

  def allRecords: (String, FlightSnapshot) => Boolean = (_, _) => true

}
