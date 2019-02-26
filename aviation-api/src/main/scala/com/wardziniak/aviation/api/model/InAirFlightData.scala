package com.wardziniak.aviation.api.model

case class InAirFlightData(lastTimeStamp: Long, flightInfo: List[FlightSnapshot]) {
  def addSnapshot(flightSnapshot: FlightSnapshot, lastTimeStamp: Long) = copy(flightInfo = flightInfo :+ flightSnapshot, lastTimeStamp = lastTimeStamp)
}

case class FlightSnapshots(snapshots: Seq[FlightSnapshot]) {
  def addSnapshot(flightSnapshot: FlightSnapshot) = copy(snapshots = snapshots :+ flightSnapshot)
  def merge(flightSnapshots: FlightSnapshots) = copy(snapshots = snapshots ++ flightSnapshots.snapshots)
}


object InAirFlightData {
  def apply(flightSnapshot: FlightSnapshot, lastTimeStamp: Long): InAirFlightData =
    new InAirFlightData(
      lastTimeStamp = lastTimeStamp,
      flightInfo = List(flightSnapshot))
}
