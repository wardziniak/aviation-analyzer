package com.wardziniak.aviation

package object preprocessing {

  object StoresNames {
    val InAirFlightStoreName: String = "flights-in-air-store4"
    val LandedFlightStoreName: String = "landed-flight-store4"
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

}
