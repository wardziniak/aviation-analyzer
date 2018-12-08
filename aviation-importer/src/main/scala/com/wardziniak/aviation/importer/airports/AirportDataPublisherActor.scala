package com.wardziniak.aviation.importer.airports

import com.wardziniak.aviation.api.model.Airport
import com.wardziniak.aviation.common.serialization.GenericSerializer
import com.wardziniak.aviation.importer.api.RawDataPublisherActor
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer

class AirportDataPublisherActor(override val kafkaServer: String, override val topic: String) extends RawDataPublisherActor[String, Airport] {
  override val keyExtractor: Airport => String = airport => airport.codeIcao
  override val producer: KafkaProducer[String, Airport] =
    new KafkaProducer[String, Airport](props, new StringSerializer(), new GenericSerializer[Airport])
}
