package com.wardziniak.aviation.common

import java.util.Properties

import com.wardziniak.aviation.api.model.Airport
import com.wardziniak.aviation.common.serialization.GenericSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.StringSerializer

trait AirportKafkaDataPublisher extends KafkaDataPublisher[String, Airport]

trait DefaultAirportKafkaDataPublisher extends AirportKafkaDataPublisher {

  def kafkaServer: String = KafkaDataPublisher.KafkaDefaultServer

  val props: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")
    props
  }

  override val keyExtractor: Airport => String = airport => airport.codeIata
  override val producer: KafkaProducer[String, Airport] =
    new KafkaProducer[String, Airport](props, new StringSerializer(), new GenericSerializer[Airport])
}