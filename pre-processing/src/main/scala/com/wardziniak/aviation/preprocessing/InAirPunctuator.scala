package com.wardziniak.aviation.preprocessing

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{Airport, InAirFlightData}
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.{ProcessorContext, Punctuator}
import org.apache.kafka.streams.state.{KeyValueIterator, KeyValueStore}

/**
  * This Punctuator is called by every Interval by scheduler set in InAirTransformer,
  * it check if there are any "old" flightSnapshot (that means for LANDING_TIMEOUT there wasn't any new information)
  * old flightSnapshot is treated as landed and all flightSnapshot for same flight are updated and passed forward
  * @param context - context passed by Transformer
  * @param inAirFlightsStoreName - name of store with information with flightStanpshot, that are in air
  * @param airportStoreName - name of store with airport
  */
case class InAirPunctuator(
  context: ProcessorContext,
  inAirFlightsStoreName: String,
  airportStoreName: String
)
  extends Punctuator
    with LazyLogging {

  val LANDING_TIMEOUT = 600000
  val SECONDS_TO_MS = 1000

  val airportStore: KeyValueStore[String, Airport] = context.getStateStore(airportStoreName).asInstanceOf[KeyValueStore[String, Airport]]
  var inAirStore: KeyValueStore[String, InAirFlightData] = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, InAirFlightData]]

  override def punctuate(timestamp: Long): Unit = {
    logger.info(s"running punctuate with timestamp = $timestamp")
    import collection.JavaConverters._
    val flightInAirIterator: KeyValueIterator[String, InAirFlightData] = inAirStore.all()
    val toRemove: Seq[KeyValue[String, InAirFlightData]] = flightInAirIterator
      .asScala
      .filter(_.value.lastTimeStamp * SECONDS_TO_MS + LANDING_TIMEOUT < timestamp)
      .map(flightData => {
        val landedSnapshot = flightData.value.flightInfo.maxBy(_.updated)
        val destinationAirport = airportStore.get(landedSnapshot.arrival.iata)
        val landedTimestamp = Helpers.calculateLandingTime(landedSnapshot, destinationAirport)
        val duplicatedValues = flightData.value.flightInfo.sortBy(_.updated).distinct
        duplicatedValues.map(flightSnapshot => flightSnapshot.withLandingData(destinationAirport, landedTimestamp))
            .foreach(analyticSnapshot => context.forward(analyticSnapshot.flightNumber.iata, analyticSnapshot))
        logger.debug(s"[key=${flightData.key}],[size=${duplicatedValues.size}],[timestamp=$timestamp],[lastupdate=${flightData.value.lastTimeStamp}]")
        flightData
      }).toList
    flightInAirIterator.close()

    toRemove.foreach(sentFlights => inAirStore.delete(sentFlights.key))
  }
}
