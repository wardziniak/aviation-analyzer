package com.wardziniak.aviation.importer.api

sealed trait EndpointType {
  val Name: String
}


object FlightTracker extends EndpointType {
  override val Name: String = "flights"
}

object AirlineRoutes extends EndpointType {
  override val Name: String = "routes"
}

object Airplanes extends EndpointType {
  override val Name: String = "airplaneDatabase"
}