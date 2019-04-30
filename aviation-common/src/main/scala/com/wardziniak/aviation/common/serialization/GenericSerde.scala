package com.wardziniak.aviation.common.serialization

import java.util

import com.sksamuel.avro4s._
import org.apache.kafka.common.serialization.{Serde, Serializer}

case class GenericSerde[T >: Null: Decoder: Encoder]() extends Serde[T] {

  override def deserializer(): GenericDeserializer[T] = {
    GenericDeserializer[T]()
  }

  override def serializer(): Serializer[T] = {
    GenericSerializer[T]()
  }

  override def configure(configs: util.Map[String, _], isKey: Boolean) = {}

  override def close() = {}
}
