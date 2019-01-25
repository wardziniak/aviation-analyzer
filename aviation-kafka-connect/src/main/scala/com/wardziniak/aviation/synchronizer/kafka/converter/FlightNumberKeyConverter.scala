package com.wardziniak.aviation.synchronizer.kafka.converter

import java.util

import org.apache.kafka.connect.data.{Schema, SchemaAndValue, SchemaBuilder, Struct}
import org.apache.kafka.connect.storage.Converter

class FlightNumberKeyConverter extends Converter {

  val FlightNumberFieldName: String = "flightNumber"

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def fromConnectData(topic: String, schema: Schema, value: scala.Any): Array[Byte] = {
    null
  }

  override def toConnectData(topic: String, rawValue: Array[Byte]): SchemaAndValue = {
    val flightNumber = new String(rawValue)
    val structure = SchemaBuilder.struct
    val schema: Schema = structure.field(FlightNumberFieldName, SchemaBuilder.string).build()
    val value: Struct = new Struct(structure)
    value.put(schema.field(FlightNumberFieldName), flightNumber)
    new SchemaAndValue(schema, value)
  }
}
