package com.wardziniak.aviation

package object processing {

  object nodeName {
    val RawSourceNodeName = "RawSource"
    val FlightGrouperProcessorNodeName = "FlightGrouperProcessor"
    val CleaningProcessorNodeName = "CleaningProcessor"
    val DeduplicationProcessorNodeName = "DeduplicationProcessor"
    val ImputationProcessorNodeName = "ImputationProcessor"
    val NormalizationProcessorNodeName = "NormalizationProcessor"
    val SinkProcessorNodeName = "SinkProcessor"
  }

  object storeNames {
    val FlightGrouperStoreName: String = "flight-grouper-store"
    val DeduplicationStoreName: String = "deduplication-store"
    val CleaningStoreName: String = "cleaning-store"
  }

  object topicNames {
    val RawDataInputTopic: String = "in-raw" // TODO: change
    val FlightGroupedTopic: String = "grouped-flight"
    val ProcessedDataTopic: String = "processed-data"
  }

  def flightDataTimestampToSecond(timestamp: Long): Long = timestamp / 1000
}
