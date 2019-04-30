package com.wardziniak.aviation.processing.internal

import java.time.Duration

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.processing.internal.ProcessorHelper.ProcessorWithStateStoreAndPunctuator
import org.apache.kafka.streams.processor.Punctuator
import org.apache.kafka.streams.state.KeyValueIterator

/**
  * It should group/hold all flightSnapshot, till it discover, that plane landed and add landed time to snapshots data
  */
object FlightGrouper {

  val MINIMAL_SNAPSHOT_SIZE = 5
  val EXPIRATION_TIME_MS = 6000
  val PUNCTUATOR_INTERVAL_SEC = 360

  case class FlightGrouperProcessor(expirationTimeout: Duration, stateStoreName: String)
    extends ProcessorWithStateStoreAndPunctuator[FlightNumberIata, FlightSnapshot, InAirFlightData](stateStoreName) {

    override val punctuatorInterval: Duration = Duration.ofSeconds(PUNCTUATOR_INTERVAL_SEC)

    override def punctuate(currentTimestamp: Long): Unit = {
      import collection.JavaConverters._
      val inAirFlightDataIterator: KeyValueIterator[FlightNumberIata, InAirFlightData] = stateStore.all()
      val flightDataList = inAirFlightDataIterator.asScala.toList
      inAirFlightDataIterator.close()

      val oldFlightDataList = flightDataList.filter(_.value.lastTimeStamp + expirationTimeout.toMillis < context.timestamp())
      oldFlightDataList
        .foreach(flightData => context.forward(flightData.key, flightData.value))
      oldFlightDataList.map(_.key).foreach(stateStore.delete)
    }

    override def process(key: FlightNumberIata, value: FlightSnapshot): Unit = {
      val currentTimestamp = context.timestamp()
      val newAgg: InAirFlightData = Option(stateStore.get(key))
        .map(inAirFlightData => inAirFlightData.addSnapshot(value, currentTimestamp))
        .getOrElse(InAirFlightData(value, currentTimestamp))
      stateStore.put(key, newAgg)
    }
  }

}
