package com.wardziniak.aviation

package object analyzer {

  object Stores {
    val InAirFlightStoreName: String = "flights-in-air-store4"
    val LandedFlightStoreName: String = "landed-flight-store4"
  }

  object Topics {
    val RawInputTopic = "aviation-in"
    val LandedTableTopic = "landed-table"
    val LandedTopic = "landed"
    val AirportsTopic = "airports" // Should be compacted topic
    val InAirWithLandTimeTopic = "in-air-with-landed-time"
    val ErrorTopic = "errors"


    val MainInputTopic = "single-part-in"
    val InAirAfterLandedTopic = "in-air-after-landed"
    val TempTopic = "temp1"
  }
}
