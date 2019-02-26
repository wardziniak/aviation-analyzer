package com.wardziniak.aviation

package object processing {

  object storeNames {
    val FlightGrouperStoreName: String = "flight-grouper-store"
    val DeduplicationStoreName: String = "deduplication-store"
    val CleaningStoreName: String = "cleaning-store"
  }

  object topicNames {
    val RawDataInputTopic: String = "in-raw" // TODO: change
    val FlightGroupedTopic: String = "grouped-flight"
  }
}
