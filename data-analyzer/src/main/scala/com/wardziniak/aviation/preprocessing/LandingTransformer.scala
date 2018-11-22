package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.FlightSnapshot
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{ProcessorContext, PunctuationType}
import org.apache.kafka.streams.state.KeyValueStore

case class LandingTransformer(inAirFlightsStoreName: String, landedFlightStoreName: String)
  extends Transformer[String, FlightSnapshot, (String, FlightSnapshot)]
    with LazyLogging {

  val LANDING_DATA_TIMEOUT_MS = 1800000

  val PUNCTUATION_INTERVAL = 60000

  var inAirStore: KeyValueStore[String, FlightSnapshot] = _
  var landedStore: KeyValueStore[String, FlightSnapshot] = _

  override def init(context: ProcessorContext): Unit = {
    logger.info(s"Init LandingTransformer")
    inAirStore = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, FlightSnapshot]]
    landedStore = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, FlightSnapshot]]
    //context.schedule(PUNCTUATION_INTERVAL, PunctuationType.WALL_CLOCK_TIME, InAirPunctuator(context, landingStoreName))
  }

  override def transform(icao: String, flightSnapshot: FlightSnapshot): (String, FlightSnapshot) = {
//    val toForward = Option(state.get(icao))
//      .find(previous => previous.updated + LANDING_DATA_TIMEOUT_MS > flightSnapshot.updated)
//    if (toForward.isEmpty) {
//      logger.info(s"LandingTransformer:[$icao], [$flightSnapshot]")
//      state.put(icao, flightSnapshot)
//      (icao, flightSnapshot)
//    }
//    else {
//      logger.info(s"LandingTransformer:Already_landed[$icao], [$flightSnapshot]")
      null
    }

  override def close(): Unit = {
    logger.info(s"Closing InAirTransformer")
  }
}
