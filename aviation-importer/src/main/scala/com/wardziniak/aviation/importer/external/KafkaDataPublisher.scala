package com.wardziniak.aviation.importer.external

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.{FlightSnapshot, Value}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.{ExecutionContext, Future}

trait KafkaDataPublisher[KEY, VALUE <: Value] extends LazyLogging {

  val producer: KafkaProducer[KEY, VALUE]

  val keyExtractor: VALUE => KEY

  //implicit val executor: ExecutionContext

  def publish(topic: String, records: Seq[VALUE])(implicit executor: ExecutionContext) = {
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
  override val keyExtractor: FlightSnapshot => String = flight => flight.flightNumber.icao
}
