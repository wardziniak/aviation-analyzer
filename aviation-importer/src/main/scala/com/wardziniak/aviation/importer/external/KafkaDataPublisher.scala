package com.wardziniak.aviation.importer.external

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{FlightSnapshot, Value}
import com.wardziniak.aviation.common.serialization.GenericSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ExecutionContext, Future}

trait KafkaDataPublisher[KEY, VALUE <: Value] extends LazyLogging {

  val producer: KafkaProducer[KEY, VALUE]

  val keyExtractor: VALUE => KEY

  def publish(topic: String, records: Seq[VALUE])(implicit executor: ExecutionContext): Future[Seq[RecordMetadata]] = {
    val results = records
      .map(value => new ProducerRecord[KEY, VALUE](topic, keyExtractor(value), value))
      .map(record => Future {
        logger.error("Sent message")
        producer.send(record).get()
      }
    )
    Future.sequence(results)
  }
}

trait BasicKafkaDataPublisher[VALUE <: Value] extends KafkaDataPublisher[String, VALUE]

trait FlightSnapshotKafkaDataPublisher extends KafkaDataPublisher[String, FlightSnapshot]

trait DefaultFlightSnapshotKafkaDataPublisher extends FlightSnapshotKafkaDataPublisher {

  def kafkaServer: String = "localhost:9092"

  val props: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")
    props
  }

//  def defaultProperties(kafkaServer: String) = {
//    val props = new Properties()
//    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
//    props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId")
//  }
//
//  def this(kafkaServer: String) = {
//    this(defaultProperties(kafkaServer))
//  }

  override val keyExtractor: FlightSnapshot => String = flight => flight.flightNumber.icao
  override val producer: KafkaProducer[String, FlightSnapshot] =
    new KafkaProducer[String, FlightSnapshot](props, new StringSerializer(), new GenericSerializer[FlightSnapshot])
}
