package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.InAirFlightData
import org.apache.kafka.streams.processor.AbstractProcessor

/**
  * Punctuator with cleaning should take few snapshot for same flight and try to figure out if all are ok
  * Drop data if:
  * 1. Destination airport has changed
  * 2. Departure airport has changed
  * 3. Not enough data is grouped < 5~10
  */

object Cleaning {

  val MinimalGroupSize = 8

  val SameDestinationAirportPredicate: InAirFlightData => Boolean =
    inAirFlightData => inAirFlightData.flightInfo.map(_.arrival.iata).distinct.size == 1

  val SameDepartureAirportPredicate: InAirFlightData => Boolean =
    inAirFlightData => inAirFlightData.flightInfo.map(_.departure.iata).distinct.size == 1

  val GroupSizePredicate: InAirFlightData => Boolean =
    inAirFlightData => inAirFlightData.flightInfo.size > MinimalGroupSize

  lazy val CleaningPredicates: Seq[InAirFlightData => Boolean] = Seq(
    SameDestinationAirportPredicate, SameDepartureAirportPredicate, GroupSizePredicate
  )

  case class CleaningProcessor() extends AbstractProcessor[FlightNumberIata, InAirFlightData] {
    override def process(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): Unit = {
      CleaningPredicates
        .foldLeft(Some(inAirFlightData): Option[InAirFlightData])((optInAirFlightData, predicate) => optInAirFlightData.filter(predicate))
        .foreach(inAirFlightData => context().forward(flightNumberIata, inAirFlightData))
    }
  }

}