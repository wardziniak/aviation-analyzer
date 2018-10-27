package com.wardziniak.aviation.importer.api

import java.util.Properties

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.Value
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

trait RawDataPublisherActor[KEY, V <: Value]
  extends Actor with LazyLogging {

  lazy val props: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")
    props
  }

  val kafkaServer: String = "localhost:9092"
  val topic: String
  val producer: KafkaProducer[KEY, V]

  val keyExtractor: V => KEY

  override def receive: Receive = {
    case flightSnapshot: V =>
      logger.trace(s"Carriage Information [$flightSnapshot]")
      producer.send(new ProducerRecord[KEY, V](topic, keyExtractor(flightSnapshot), flightSnapshot))
    case p =>
      logger.error(s"Unkown type [$p]")
  }

}
