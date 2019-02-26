package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.InAirFlightData
import org.apache.kafka.streams.kstream.ValueTransformerWithKey
import org.apache.kafka.streams.processor.ProcessorContext

/**
  * Punctuator with cleaning should take few snapshot for same flight and try to figure out if all are ok
  */

object Cleaning {

  case class CleaningTransformer()
    extends ValueTransformerWithKey[FlightNumberIata, InAirFlightData, InAirFlightData] {

    override def init(context: ProcessorContext): Unit = {}

    override def close(): Unit = {}

    override def transform(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): InAirFlightData = {
      // TODO check flight pattern, if following snapshot are in proper route
      val uniqueDeparture = inAirFlightData.flightInfo.map(_.departure.iata).distinct.size
      val arrivalDeparture = inAirFlightData.flightInfo.map(_.arrival.iata).distinct.size
      if (uniqueDeparture == 1 && arrivalDeparture == 1)
        inAirFlightData
      else
        null
    }
  }
}
