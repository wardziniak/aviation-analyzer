package com.wardziniak.aviation.processing.internal

import java.time.Duration

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import org.apache.kafka.streams.processor.AbstractProcessor

object InAirDataFlatten {

  case class InAirDataFlattenProcessor(windowsSize: Duration = ???) extends AbstractProcessor[FlightNumberIata, InAirFlightData] {
    override def process(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): Unit = {
      // It should forward [FlightNumberIata, FlightSnapshot]
    }

    private def calculateMidSnapshot(begin: FlightSnapshot, end: FlightSnapshot): FlightSnapshot = ???
  }

}
