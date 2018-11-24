package com.wardziniak.aviation.utils

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.streams.TopologyTestDriver

object MessageStreamReader extends LazyLogging {

  def messageStream[KEY, VALUE](
    topic: String,
    testDriver: TopologyTestDriver,
    keySerializer: Deserializer[KEY],
    valueSerializer: Deserializer[VALUE]): Stream[ProducerRecord[KEY, VALUE]] = {
    val firstElement = testDriver.readOutput(topic, keySerializer, valueSerializer)
    if (firstElement == null)
      Stream.empty[ProducerRecord[KEY, VALUE]]
    else
      firstElement #:: messageStream(topic, testDriver, keySerializer, valueSerializer)
  }
}
