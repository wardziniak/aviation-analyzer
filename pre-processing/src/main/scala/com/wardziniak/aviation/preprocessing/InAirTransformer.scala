package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{ProcessorContext, PunctuationType}
import org.apache.kafka.streams.state.KeyValueStore

case class InAirTransformer(inAirFlightsStoreName: String, landingStoreName: String)
  extends Transformer[String, FlightSnapshot, (String, FlightSnapshot)]
    with LazyLogging {

  val PUNCTUATION_INTERVAL = 60000//3600000

  var inAirStore: KeyValueStore[String, InAirFlightData] = _

  var state: KeyValueStore[String, InAirFlightData] = _

  override def init(context: ProcessorContext): Unit = {
    logger.info(s"Init InAirTransformer")
    inAirStore = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, InAirFlightData]]
    context.schedule(PUNCTUATION_INTERVAL, PunctuationType.WALL_CLOCK_TIME, InAirPunctuator(context, inAirFlightsStoreName, landingStoreName))
  }

  override def transform(icao: String, flightSnapshot: FlightSnapshot): (String, FlightSnapshot) = {
    Option(inAirStore.get(icao))
      .map(_.addSnapshot(flightSnapshot))
      .orElse(Some(InAirFlightData(flightSnapshot)))
      .foreach(inAirStore.put(icao, _))
    null
  }

  override def close(): Unit = {
    logger.info(s"Closing InAirTransformer")
  }
}
