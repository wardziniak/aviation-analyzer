package com.wardziniak.aviation.common.serialization

import java.util

import com.sksamuel.avro4s.{ FromRecord, SchemaFor }
import org.apache.kafka.common.serialization.Deserializer

case class GenericDeserializer[T: SchemaFor: FromRecord]() extends Deserializer[T] {

  override def configure(configs: util.Map[String, _], isKey: Boolean) = {}

  override def close() = {}

  override def deserialize(topic: String, data: Array[Byte]): T = {
    JsonAvroSerializer.deserialize[T](data)
  }
}