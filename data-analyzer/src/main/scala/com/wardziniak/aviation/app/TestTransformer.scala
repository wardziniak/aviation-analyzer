package com.wardziniak.aviation.app

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.app.TestStoreApp.Person
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{Processor, ProcessorContext}
import org.apache.kafka.streams.state.KeyValueStore

case class TestTransformer (personStore: String)
  extends Transformer[String, String, (String, String)]
    with LazyLogging {

  val LANDING_DATA_TIMEOUT_MS = 1800000

  val PUNCTUATION_INTERVAL = 60000

  var state: KeyValueStore[String, Person] = _

  override def init(context: ProcessorContext): Unit = {
    logger.info(s"Init LandingTransformer")
    state = context.getStateStore(personStore).asInstanceOf[KeyValueStore[String, Person]]
  }

  override def transform(key: String, value: String): (String, String) = {
    state.put(key, Person(3, key, value))
    null
  }

  override def close(): Unit = {
    logger.info(s"Closing InAirTransformer")
  }
}

