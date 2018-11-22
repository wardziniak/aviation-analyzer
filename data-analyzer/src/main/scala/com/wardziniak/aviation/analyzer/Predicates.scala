package com.wardziniak.aviation.analyzer

import com.wardziniak.aviation.api.model.FlightSnapshot

object Predicates {


  def properFlightSnapshots: (String, FlightSnapshot) => Boolean = (icao: String, flightSnapshot: FlightSnapshot) => {
    flightSnapshot.airlineCode != null &&
    flightSnapshot.departure != null
    true
  }

}
