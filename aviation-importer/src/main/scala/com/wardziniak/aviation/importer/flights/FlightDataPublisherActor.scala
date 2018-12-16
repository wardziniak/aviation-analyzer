package com.wardziniak.aviation.importer.flights

import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericSerializer
import com.wardziniak.aviation.importer.api.RawDataPublisherActor
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer

case class FlightDataPublisherActor(override val kafkaServer: String, override val topic: String) extends RawDataPublisherActor[String, FlightSnapshot] {
  override val keyExtractor: FlightSnapshot => String = flight => flight.flightNumber.iata
  override val producer: KafkaProducer[String, FlightSnapshot] =
    new KafkaProducer[String, FlightSnapshot](props, new StringSerializer(), new GenericSerializer[FlightSnapshot])
}
