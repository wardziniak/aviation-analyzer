package com.wardziniak.aviation.common.serialization

import java.io.ByteArrayOutputStream

import com.sksamuel.avro4s._
import org.apache.kafka.common.errors.SerializationException

object JsonAvroSerializer {
  def serialize[T: Decoder: Encoder](value: T): Array[Byte] = {
    Option(value)
      .map(v => {
        val baos = new ByteArrayOutputStream()
        AvroOutputStream.json[T].to(baos)
        baos.toByteArray
      })
      .getOrElse(Array.emptyByteArray)
  }

  def deserialize[T >: Null: Decoder](bytes: Array[Byte]): T = {
    Option(bytes).map(AvroInputStream.json[T].from)
      .map(_.build.tryIterator.toSeq.head)
      .map(_.getOrElse(throw new SerializationException(s"Cannot deserialize [$bytes]")))
        .orNull
  }
}
