package com.wardziniak.aviation.common

import java.util.Properties

import com.wardziniak.aviation.api.model.Aircraft
import com.wardziniak.aviation.common.serialization.GenericSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.StringSerializer

trait DefaultAircraftKafkaDataPublisher extends KafkaDataPublisher[String, Aircraft] {
  def kafkaServer: String = KafkaDataPublisher.KafkaDefaultServer

  val props: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")
    props
  }

  override val keyExtractor: Aircraft => String = aircraft => aircraft.numberRegistration
  override val producer: KafkaProducer[String, Aircraft] =
    new KafkaProducer[String, Aircraft](props, new StringSerializer(), new GenericSerializer[Aircraft])
}