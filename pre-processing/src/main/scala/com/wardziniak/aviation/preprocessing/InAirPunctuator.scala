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


  logger.info(s"Init::InAirPunctuator")

  val LANDING_TIMEOUT = 20 * 60 * 1000
  val MINIMAL_LANDING_ALTITUDE = 6000
  val SECONDS_TO_MS = 1000

  val airportStore: KeyValueStore[String, Airport] = context.getStateStore(airportStoreName).asInstanceOf[KeyValueStore[String, Airport]]
  var inAirStore: KeyValueStore[String, InAirFlightData] = context.getStateStore(inAirFlightsStoreName).asInstanceOf[KeyValueStore[String, InAirFlightData]]

  // Jesli samolot jest "wysoko", to znaczy,
  // że nie mógł jeszcze wyladować, więc chociaż zniknął na chwilę, to nic nie robimy.
  // Druga wartość "długości" nie widzenia snapshotu samolotu, po którym uznajemy, że "zaginął" i nie sledzimy go

  override def punctuate(timestamp: Long): Unit = {
    logger.info(s"running punctuate with timestamp = $timestamp")
    import collection.JavaConverters._
    val flightInAirIterator: KeyValueIterator[String, InAirFlightData] = inAirStore.all()

    val inAirPlanes = flightInAirIterator.asScala.toList
    val landedAirplanes = inAirPlanes.filter(inAirFlightData => Helpers.isAirplaneLand(inAirFlightData.value, airportStore, timestamp))
      .map(flightData => {
        val destinationAirport = airportStore.get(flightData.value.flightInfo.head.arrival.iata)
        val landedSnapshot = Helpers.findLandedSnapshot(flightData.value.flightInfo, destinationAirport)
        val landedTimestamp = Helpers.calculateLandingTime(landedSnapshot, destinationAirport)
        val duplicatedValues = flightData.value.flightInfo.sortBy(_.updated).distinct

        Option(destinationAirport).foreach(destAirport =>
          duplicatedValues.map(flightSnapshot => flightSnapshot.withLandingData(destAirport, landedTimestamp))
            .foreach(analyticSnapshot => {
              logger.info(s"forward: ${analyticSnapshot.flightNumber.iata}, $analyticSnapshot")
              context.forward(analyticSnapshot.flightNumber.iata, analyticSnapshot)
            }
            )
        )
        flightData
      })
    val lostAirplanes = inAirPlanes.filter(inAirFlightData => Helpers.isAirplaneLost(inAirFlightData.value, timestamp))

    flightInAirIterator.close()

    landedAirplanes.foreach(landedAirplane => inAirStore.delete(landedAirplane.key))
    lostAirplanes.foreach(lostAirplane => logger.error(s"Lost following airplane [$lostAirplane]"))
    lostAirplanes.foreach(lostAirplane => inAirStore.delete(lostAirplane.key))
  }
}
