package com.wardziniak.aviation.common.serialization

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream }

import com.sksamuel.avro4s._

object JsonAvroSerializer {
  def serialize[T: SchemaFor: ToRecord](value: T): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val output = AvroOutputStream.json[T](baos)
    output.write(value)
    output.close()
    baos.toByteArray
  }

  def deserialize[T: SchemaFor: FromRecord](bytes: Array[Byte]): T = {
    val in = new ByteArrayInputStream(bytes)
    val input = AvroInputStream.json[T](in)
    input.singleEntity.get
  }
}
