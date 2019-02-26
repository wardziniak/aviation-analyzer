package com.wardziniak.aviation.processing.internal

import java.nio.ByteBuffer
import java.time.Duration

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{ProcessorContext, PunctuationType, Punctuator}
import org.apache.kafka.streams.state.{KeyValueIterator, KeyValueStore}

/**
  * It should group/hold all flightsnapshot, till it discover, that plane landed
  */
object FlightGrouper {

  val MINIMAL_SNAPSHOT_SIZE = 5
  val EXPIRATION_TIME_MS = 6000
  val PUNCTUATOR_INTERVAL_SEC = 360

  case class FlightGrouperTransformer(
    expirationTimeout: Duration,
    inAirFLightDataStoreName: String)
    extends Transformer[FlightNumberIata, FlightSnapshot, KeyValue[FlightNumberIata, InAirFlightData]] {

    var inAirFLightDataStore: KeyValueStore[FlightNumberIata, InAirFlightData] = _
    var context: ProcessorContext = _

    override def init(context: ProcessorContext): Unit = {
      this.context = context
      inAirFLightDataStore = context.getStateStore(inAirFLightDataStoreName).asInstanceOf[KeyValueStore[FlightNumberIata, InAirFlightData]]
      context.schedule(
        Duration.ofSeconds(PUNCTUATOR_INTERVAL_SEC),
        PunctuationType.WALL_CLOCK_TIME,
        FlightGrouperPunctuator(
          inAirFLightDataStoreName = inAirFLightDataStoreName,
          expirationTimeout = expirationTimeout,
          context = context)
      )
    }

    override def transform(key: FlightNumberIata, value: FlightSnapshot): KeyValue[FlightNumberIata, InAirFlightData] = {
      val currentTimestamp = context.timestamp()
      val newAgg: InAirFlightData = Option(inAirFLightDataStore.get(key))
        .map(inAirFlightData => inAirFlightData.addSnapshot(value, currentTimestamp))
        .getOrElse(InAirFlightData(value, currentTimestamp))
      inAirFLightDataStore.put(key, newAgg)
      null
    }

    override def close(): Unit = {}
  }

  def extractTimestampWithAgg(aggValueSerde: Serde[InAirFlightData])(rawAgg: Bytes): (Long, InAirFlightData) = {
    val byteBufferAggWithTimestamp = ByteBuffer.wrap(rawAgg.get())
    val lastUpdateTimestamp = byteBufferAggWithTimestamp.getLong
    val aggValueBytes = new Array[Byte](rawAgg.get().length - 8)
    byteBufferAggWithTimestamp.get(aggValueBytes)
    val oldAgg = aggValueSerde.deserializer().deserialize(null, aggValueBytes)
    (lastUpdateTimestamp, oldAgg)
  }

  case class FlightGrouperPunctuator(
    inAirFLightDataStoreName: String,
    expirationTimeout: Duration,
    context: ProcessorContext) extends Punctuator {

    var inAirFLightDataStore: KeyValueStore[FlightNumberIata, InAirFlightData] = context.getStateStore(inAirFLightDataStoreName).asInstanceOf[KeyValueStore[FlightNumberIata, InAirFlightData]]

    override def punctuate(currentTimestamp: Long): Unit = {
      import collection.JavaConverters._
      val inAirFlightDataIterator: KeyValueIterator[FlightNumberIata, InAirFlightData] = inAirFLightDataStore.all()

      val flightDataList = inAirFlightDataIterator.asScala.toList
      val oldFlightDataList = flightDataList.filter(_.value.lastTimeStamp + expirationTimeout.toMillis < context.timestamp())

      // At least MINIMAL_SNAPSHOT_SIZE has to be grouped to pass. If it less and data expired drop them.
      oldFlightDataList
        .filter(_.value.flightInfo.size > MINIMAL_SNAPSHOT_SIZE)
        .foreach(flightData => context.forward(flightData.key, flightData.value))

      inAirFlightDataIterator.close()
      oldFlightDataList.map(_.key).foreach(inAirFLightDataStore.delete)
    }
  }
}
