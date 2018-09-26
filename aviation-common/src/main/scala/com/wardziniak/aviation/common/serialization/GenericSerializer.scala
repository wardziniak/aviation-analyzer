package com.wardziniak.aviation.common.serialization

import java.util

import com.sksamuel.avro4s.{ SchemaFor, ToRecord }
import org.apache.kafka.common.serialization.Serializer

case class GenericSerializer[T: SchemaFor: ToRecord]() extends Serializer[T] {

  override def configure(configs: util.Map[String, _], isKey: Boolean) = {}

  override def serialize(topic: String, data: T): Array[Byte] = {
    JsonAvroSerializer.serialize[T](data)
  }

  override def close() = {}
}
