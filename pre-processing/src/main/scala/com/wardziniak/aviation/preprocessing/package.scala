package com.wardziniak.aviation

package object preprocessing {

  object StoresNames {
    val InAirFlightStoreName: String = "flights-in-air-store4"
    val LandedFlightStoreName: String = "landed-flight-store4"
  }

  object TopicsNames {
    val RawInputTopic = "aviation-in"
    val LandedTableTopic = "landed-table"
    val LandedTopic = "landed"
    val AirportsTopic = "airports" // Should be compacted topic
    val InAirWithLandedDataTopic = "in-air-with-landed-time"
    val ErrorTopic = "errors"


    val MainInputTopic = "single-part-in"
    val InAirAfterLandedTopic = "in-air-after-landed"
    val TempTopic = "temp1"
  }

}
