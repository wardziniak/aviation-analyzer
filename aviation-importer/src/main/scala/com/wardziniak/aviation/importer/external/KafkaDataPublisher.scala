package com.wardziniak.aviation.importer.external

import com.wardziniak.aviation.api.model.Value
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

trait KafkaDataPublisher[VALUE <: Value, KEY] {

  def publish(keyExtractor: VALUE => KEY)(producer: KafkaProducer[KEY, VALUE], topic: String, records: Seq[VALUE]) = {
    records
      .map(value => new ProducerRecord[KEY, VALUE](topic, keyExtractor(value), value))
      .map(record => producer.send(record))
  }
}
