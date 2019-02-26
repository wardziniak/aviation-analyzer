package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.InAirFlightData
import org.apache.kafka.streams.kstream.ValueTransformerWithKey
import org.apache.kafka.streams.processor.ProcessorContext

object Deduplication {

  case class DeduplicationTransformer()
    extends ValueTransformerWithKey[FlightNumberIata, InAirFlightData, InAirFlightData] {

    override def init(context: ProcessorContext): Unit = {}

    override def close(): Unit = {}

    override def transform(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): InAirFlightData = {
      val deduplicated = inAirFlightData
        .flightInfo
        .groupBy(_.updated).map(_._2.head).toList
      InAirFlightData(lastTimeStamp = inAirFlightData.lastTimeStamp, flightInfo = deduplicated)
    }
  }
}
