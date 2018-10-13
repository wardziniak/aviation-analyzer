package com.wardziniak.aviation.common

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.api.model.Value
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{ExecutionContext, Future}

trait KafkaDataPublisher[KEY, VALUE <: Value] extends LazyLogging {

  val producer: KafkaProducer[KEY, VALUE]

  val keyExtractor: VALUE => KEY

  def publish(topic: String, records: Seq[VALUE])(implicit executor: ExecutionContext): Future[Seq[RecordMetadata]] = {
    val results = records
      .map(value => new ProducerRecord[KEY, VALUE](topic, keyExtractor(value), value))
      .map(record => Future {
        logger.info(s"flight: $record")
        producer.send(record).get()
      }
    )
    Future.sequence(results)
  }
}

trait BasicKafkaDataPublisher[VALUE <: Value] extends KafkaDataPublisher[String, VALUE]

object KafkaDataPublisher {
  val KafkaDefaultServer = "localhost:9092"
}