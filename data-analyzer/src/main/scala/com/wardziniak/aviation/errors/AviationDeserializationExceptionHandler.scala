package com.wardziniak.aviation.errors

import java.util

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.streams.errors.DeserializationExceptionHandler
import org.apache.kafka.streams.errors.DeserializationExceptionHandler.DeserializationHandlerResponse
import org.apache.kafka.streams.processor.ProcessorContext

class AviationDeserializationExceptionHandler
  extends DeserializationExceptionHandler
    with LazyLogging {

  override def handle(context: ProcessorContext, record: ConsumerRecord[Array[Byte], Array[Byte]], exception: Exception): DeserializationExceptionHandler.DeserializationHandlerResponse = {
    logger.warn("Exception caught during Deserialization, " + "taskId: {}, topic: {}, partition: {}, offset: {}", context.taskId, record.topic, record.partition, record.offset, exception)
    DeserializationHandlerResponse.CONTINUE
  }

  override def configure(configs: util.Map[String, _]): Unit = {}
}
