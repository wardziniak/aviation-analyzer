package com.wardziniak.aviation.common

import java.util.Properties

import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.StringSerializer

trait FlightSnapshotKafkaDataPublisher extends KafkaDataPublisher[String, FlightSnapshot]

trait DefaultFlightSnapshotKafkaDataPublisher extends FlightSnapshotKafkaDataPublisher {

  def kafkaServer: String = KafkaDataPublisher.KafkaDefaultServer

  val props: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")
    props
  }

  override val keyExtractor: FlightSnapshot => String = flight => flight.flightNumber.icao
  override val producer: KafkaProducer[String, FlightSnapshot] =
    new KafkaProducer[String, FlightSnapshot](props, new StringSerializer(), new GenericSerializer[FlightSnapshot])
}
