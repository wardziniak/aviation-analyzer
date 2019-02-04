package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{Airport, AnalyticFlightSnapshot, FlightSnapshot, InAirFlightData}
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{ProcessorContext, PunctuationType}
import org.apache.kafka.streams.state.KeyValueStore

case class InAirTransformer(inAirFlightsStoreName: String, airportStoreName: String)
  extends Transformer[String, FlightSnapshot, KeyValue[String, AnalyticFlightSnapshot]]
    with LazyLogging {

  val PUNCTUATION_INTERVAL = 6 * 60 * 1000// every 6 minutes

  var inAirStore: KeyValueStore[String, InAirFlightData] = _
  var airportStore: KeyValueStore[String, Airport] = _

  var state: KeyValueStore[String, InAirFlightData] = _
  var context: ProcessorContext = _

  override def init(context: ProcessorContext): Unit = {
    logger.info(s"Init InAirTransformer")
    inAirStore = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, InAirFlightData]]
    airportStore = context.getStateStore(airportStoreName).asInstanceOf[KeyValueStore[String, Airport]]
    this.context = context
    context.schedule(
      PUNCTUATION_INTERVAL,
      PunctuationType.WALL_CLOCK_TIME,
      InAirPunctuator(context, inAirFlightsStoreName, airportStoreName)
    )
  }

  override def transform(iata: String, flightSnapshot: FlightSnapshot): KeyValue[String, AnalyticFlightSnapshot] = {
    if (flightSnapshot.updated * 1000 + ORPHAN_RECORD_TIMEOUT_MS > context.timestamp() &&
      airportStore.get(flightSnapshot.departure.iata) != null &&
      airportStore.get(flightSnapshot.arrival.iata) != null) {
      Option(inAirStore.get(iata))
        .map(_.addSnapshot(flightSnapshot))
        .orElse(Some(InAirFlightData(flightSnapshot)))
        .foreach(inAirStore.put(iata, _))
    }
    null
  }

  override def close(): Unit = {
    logger.info(s"Closing InAirTransformer")
  }
}
