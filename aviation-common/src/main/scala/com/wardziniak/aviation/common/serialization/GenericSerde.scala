package com.wardziniak.aviation.common.serialization

import java.util

import com.sksamuel.avro4s.{ FromRecord, SchemaFor, ToRecord }
import org.apache.kafka.common.serialization.{ Serde, Serializer }

case class GenericSerde[T >: Null: SchemaFor: ToRecord: FromRecord]() extends Serde[T] {

  override def deserializer(): GenericDeserializer[T] = {
    GenericDeserializer[T]()
  }

  override def serializer(): Serializer[T] = {
    GenericSerializer[T]()
  }

  override def configure(configs: util.Map[String, _], isKey: Boolean) = {}

  override def close() = {}
}
