package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.InAirFlightData
import org.apache.kafka.streams.processor.AbstractProcessor

/**
  * Punctuator with cleaning should take few snapshot for same flight and try to figure out if all are ok
  * Drop data if:
  * 1. Destination airport has changed
  * 2. Not enough data is grouped < 5~10
  */

object Cleaning {
  case class CleaningProcessor() extends AbstractProcessor[FlightNumberIata, InAirFlightData] {
    override def process(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): Unit = {
      // TODO check flight pattern, if following snapshot are in proper route
      val uniqueDeparture = inAirFlightData.flightInfo.map(_.departure.iata).distinct.size
      val arrivalDeparture = inAirFlightData.flightInfo.map(_.arrival.iata).distinct.size
      if (uniqueDeparture == 1 && arrivalDeparture == 1)
        context().forward(flightNumberIata, inAirFlightData)
    }
  }
}