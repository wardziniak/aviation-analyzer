package com.wardziniak.aviation.api.model

object ModelUtils {

  def calculateDistance(fromLatitude: Double, fromLongitude: Double, toLatitude: Double, toLongitude: Double, airplaneAltitude: Double): Double = {
    val R = 6371
    // Radius of the earth
    val latDistance = Math.toRadians(toLatitude - fromLatitude)
    val lonDistance = Math.toRadians(toLongitude - fromLongitude)
    val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(fromLatitude)) * Math.cos(Math.toRadians(toLatitude)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    var distance = R * c * 1000
    // convert to meters
    distance = Math.pow(distance, 2) + Math.pow(airplaneAltitude, 2)
    Math.sqrt(distance)
  }

  def calculateDistanceToAirport(snapshot: FlightSnapshot, arrivalAirport: Airport): Double =
    ModelUtils.calculateDistance(
      fromLatitude = snapshot.localization.latitude,
      fromLongitude = snapshot.localization.longitude,
      toLatitude = arrivalAirport.latitude,
      toLongitude = arrivalAirport.longitude,
      airplaneAltitude = snapshot.localization.altitude
    )
}
