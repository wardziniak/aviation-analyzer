package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.{ProcessorContext, Punctuator}
import org.apache.kafka.streams.state.{KeyValueIterator, KeyValueStore}

/**
  * This Punctuator is called by every Interval by scheduler set in InAirTransformer,
  * it check if there are any "old" flightSnapshot (that means for LANDING_TIMEOUT there wasn't any new information)
  * @param context - context passed by Transformer
  * @param inAirFlightsStoreName - name of store with information with flightStanpshot, that are in air
  * @param landedFlightStoreName - name of store with last flightSnapshot - landed snapshot
  */
case class InAirPunctuator(
  context: ProcessorContext,
  inAirFlightsStoreName: String,
  landedFlightStoreName: String
)
  extends Punctuator
    with LazyLogging {

  val LANDING_TIMEOUT = 900000
  val SECONDS_TO_MS = 1000

  var inAirStore: KeyValueStore[String, InAirFlightData] = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, InAirFlightData]]
  var landedStore: KeyValueStore[String, FlightSnapshot] = context.getStateStore(landedFlightStoreName).asInstanceOf[KeyValueStore[String, FlightSnapshot]]

  override def punctuate(timestamp: Long): Unit = {
    logger.info(s"running punctuate with timestamp = $timestamp")
    import collection.JavaConverters._
    val flightInAirIterator: KeyValueIterator[String, InAirFlightData] = inAirStore.all()
    val toRemove: Seq[KeyValue[String, InAirFlightData]] = flightInAirIterator
      .asScala
      .filter(_.value.lastTimeStamp * SECONDS_TO_MS + LANDING_TIMEOUT < timestamp)
      .map(flightData => {
        logger.info(s"[key=${flightData.key}],[size=${flightData.value.flightInfo.size}],[timestamp=$timestamp],[lastupdate=${flightData.value.lastTimeStamp}]")
        val landedSnapshot = flightData.value.flightInfo.maxBy(_.updated)
        landedStore.put(flightData.key, landedSnapshot)
        flightData.value.flightInfo.foreach(flightSnapshot => context.forward(flightData.key, flightSnapshot))
        flightData
      }).toList
    flightInAirIterator.close()
    toRemove.foreach(sentFlights => inAirStore.delete(sentFlights.key))
  }
}
