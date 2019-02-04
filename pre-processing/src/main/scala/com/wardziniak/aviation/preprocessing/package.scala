package com.wardziniak.aviation

package object preprocessing {

  object StoresNames {
    val InAirFlightStoreName: String = "flights-in-air-store4"
    val LandedFlightStoreName: String = "landed-flight-store4"
    val AirportsStoreName: String = "airports-store"
  }

  object TopicsNames {
    val RawInputTopic = "aviation-in"
    val IntermediateManTopic = "intermediate-in-air-with-landed-time"
    val IntermediateLandedTableTopic = "intermediate-landed-table"
    val LandedTableTopic = "landed-table"
    val LandedTopic = "landed"
    val AirportsTopic = "airport" // Should be compacted topic
    val InAirWithLandedDataTopic = "in-air-with-landed-time"
    val ErrorTopic = "errors"


    val MainInputTopic = "single-part-in"
    val InAirAfterLandedTopic = "in-air-after-landed"
    val TempTopic = "temp1"
  }

  val LANDING_TIMEOUT: Long = 20 * 60 * 1000 // 20 minutes
  val LOST_TIMEOUT: Long = 40 * 60 * 60 * 1000  // 40 minutes
  val MINIMAL_LANDING_ALTITUDE = 6000
  val MINIMAL_LANDING_DISTANCE = 70000 // 70 km
  val LANDING_ALTITUDE = 1000
  val LANDING_DISTANCE = 2000
  val SECONDS_TO_MS = 1000

  val ORPHAN_RECORD_TIMEOUT_MS: Long = 20 * 60 * 1000 // 20 minutes

}
