package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.InAirFlightData
import org.apache.kafka.streams.processor.AbstractProcessor

object Deduplication {
    case class DeduplicationProcessor() extends AbstractProcessor[FlightNumberIata, InAirFlightData] {
    override def process(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): Unit = {
      val deduplicated = inAirFlightData
        .flightInfo
        .groupBy(_.updated).map(_._2.head).toList
      context().forward(flightNumberIata, InAirFlightData(lastTimeStamp = inAirFlightData.lastTimeStamp, flightInfo = deduplicated))
    }
  }
}
