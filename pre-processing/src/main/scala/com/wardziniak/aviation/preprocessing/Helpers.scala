package com.wardziniak.aviation.preprocessing

import com.wardziniak.aviation.api.model.{Airport, FlightSnapshot, ModelUtils, InAirFlightData}
import org.apache.kafka.streams.state.KeyValueStore

object Helpers {

  val EuropeTimezone = "Europe"

  // TODO implement way of calculating landing time based on distance, lastTimestamp, etc
  def calculateLandingTime(flightSnapshot: FlightSnapshot, airport: Airport): Long = {
    if (flightSnapshot.localization.altitude > LANDING_ALTITUDE) {
      val distance = ModelUtils.calculateDistance(flightSnapshot.localization.latitude, flightSnapshot.localization.longitude, airport.latitude, airport.longitude, flightSnapshot.localization.altitude)
      val timeToDistance = (60 * 60) * distance/(flightSnapshot.speed.horizontal * 1000)
      flightSnapshot.updated + timeToDistance.toLong
    }
    else
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

  /**
    * Algorithm of calculation if plan landed:
    * 1. If Aircraft is low enough (MINIMAL_LANDING_ALTITUDE)
    * 2. If there is not snapshot of long time (LANDING_TIMEOUT)
    * 3. All Snapshots have same destination (arrival airport - some time there disruption and arrival change for snapshot for same flight)
    * 4. If landed snapshot is near to airport (MINIMAL_DISTANCE)
    * @param inAirFlightData
    * @param airportStore
    * @param currentTimestamp
    * @return
    */
  def isAirplaneLand(inAirFlightData: InAirFlightData, airportStore: KeyValueStore[String, Airport], currentTimestamp: Long): Boolean = {
    val lastSnapshot = inAirFlightData.flightInfo.maxBy(_.updated)
    val mightLanded = lastSnapshot.updated * SECONDS_TO_MS + LANDING_TIMEOUT < currentTimestamp &&
      lastSnapshot.localization.altitude < MINIMAL_LANDING_ALTITUDE
    if (mightLanded && inAirFlightData.flightInfo.map(_.arrival.iata).distinct.size == 1) {
      val arrivalAirport: Airport = airportStore.get(inAirFlightData.flightInfo.head.arrival.iata)
      val lastSnapshot = inAirFlightData.flightInfo.maxBy(_.updated)
      val distanceToAirport = ModelUtils.calculateDistanceToAirport(lastSnapshot, arrivalAirport)
      distanceToAirport < MINIMAL_LANDING_DISTANCE
    }
    else
      false
  }

  def isAirplaneLost(inAirFlightData: InAirFlightData, currentTimestamp: Long): Boolean = {
    val lastSnapshot = inAirFlightData.flightInfo.maxBy(_.updated)
    lastSnapshot.updated * SECONDS_TO_MS + LOST_TIMEOUT < currentTimestamp
  }

  def findLandedSnapshot(snapshots: List[FlightSnapshot], airport: Airport): FlightSnapshot = {
    val landedSnapshots = snapshots.map(snapshot =>
      (ModelUtils.calculateDistance(
        snapshot.localization.latitude,
        snapshot.localization.longitude,
        airport.latitude,
        airport.longitude,
        snapshot.localization.altitude), snapshot)).filter(_._1 < LANDING_DISTANCE).map(_._2)
    val maxTimeSnapshot = snapshots.maxBy(_.updated)
    landedSnapshots.foldLeft(maxTimeSnapshot)((acc, landedSnap) => (landedSnap :: acc :: Nil).minBy(_.updated))
  }

}
