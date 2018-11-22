package com.wardziniak.aviation.api.model

case class InAirFlightData(takeOfTimestamp: Long, lastTimeStamp: Long, flightInfo: List[FlightSnapshot]) {
  def addSnapshot(flightSnapshot: FlightSnapshot) = copy(flightInfo = flightInfo :+ flightSnapshot, lastTimeStamp = flightSnapshot.updated)
}

object InAirFlightData {
  def apply(flightSnapshot: FlightSnapshot): InAirFlightData =
    new InAirFlightData(
      takeOfTimestamp = flightSnapshot.updated,
      lastTimeStamp = flightSnapshot.updated,
      flightInfo = List(flightSnapshot))
}
