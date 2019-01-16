package com.wardziniak.aviation.synchronizer.kafka.converter

import java.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node._
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.connect.data.{Schema, SchemaAndValue, SchemaBuilder, Struct}
import org.apache.kafka.connect.errors.DataException
import org.apache.kafka.connect.json.JsonDeserializer
import org.apache.kafka.connect.storage.Converter

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class AviationConverter extends Converter {
  private val deserializer = new JsonDeserializer

  private def asConnectSchema(jsonNode: JsonNode): Schema = {
    jsonNode.getNodeType
    val res = jsonNode match {
      case number: NumericNode => if (number.isIntegralNumber) SchemaBuilder.int64() else SchemaBuilder.float64()
      case _: TextNode => SchemaBuilder.string
      case _: BooleanNode => SchemaBuilder.bool
      case arrayNode: ArrayNode =>
        if (arrayNode.size() > 0) SchemaBuilder.array(asConnectSchema(arrayNode.get(0)))
        else SchemaBuilder.array(SchemaBuilder.string)
      case jsonObject: ObjectNode =>
        val structure = SchemaBuilder.struct
        jsonObject.fields.asScala.foreach(field => structure.field(field.getKey, asConnectSchema(field.getValue)))
        structure
      case _: NullNode => SchemaBuilder.string()
      case p => throw new IllegalArgumentException(s"Unknown node type [$p, ${p.getNodeType}]")
    }
    res.build()
  }

  private val TO_CONNECT_CONVERTERS: Map[JsonNodeType, ((Schema, JsonNode) => Any)] = Map(
    JsonNodeType.BOOLEAN -> ((_, node) => node.booleanValue),
    JsonNodeType.NUMBER -> ((_, node) => if (node.isIntegralNumber) node.longValue() else node.doubleValue()),
    JsonNodeType.STRING -> ((_, node) => node.textValue()),
    JsonNodeType.OBJECT -> ((schema, node) => {
      val result = new Struct(schema.schema())
      schema.fields().asScala
        .foldLeft(result)((res, field) => {
          res.put(field, convertToConnect(field.schema, node.get(field.name)))
        })
    }),
    JsonNodeType.NULL -> ((_, _) => ""),
    JsonNodeType.ARRAY -> ((schema, node) => throw new IllegalArgumentException("Unsupported node type")),
//    {
//      val elemSchema: Schema = if (schema == null) null else schema.valueSchema()
//      val result = new util.ArrayList[Any]()
//      for (elem <- node) {
//        result.add(convertToConnect(elemSchema, elem))
//      }
//      result
//    }),
    JsonNodeType.BINARY -> ((_, _) => throw new IllegalArgumentException("Unsupported node type")),
    JsonNodeType.MISSING -> ((_, _) => throw new IllegalArgumentException("Unsupported node type")),
    JsonNodeType.POJO -> ((_, _) => throw new IllegalArgumentException("Unsupported node type"))
  )

  private def convertToConnect(schema: Schema, jsonNode: JsonNode): Any = {
    TO_CONNECT_CONVERTERS.get(jsonNode.getNodeType)
      .map(_(schema, jsonNode)).get
  }

  override def configure(configs: util.Map[String, _], isKey: Boolean) = {}

  override def fromConnectData(topic: String, schema: Schema, value: scala.Any) = throw new UnsupportedOperationException

  override def toConnectData(topic: String, value: Array[Byte]): SchemaAndValue = {
    Try(deserializer.deserialize(topic, value)) match {
      case Success(jsonValue) =>
        val schema = asConnectSchema(jsonValue)
        new SchemaAndValue(schema, convertToConnect(schema, jsonValue))
      case Failure(e: SerializationException) => throw new DataException("Converting byte[] to Kafka Connect data failed due to serialization error: ", e)
      case Failure(e: Throwable) => throw e
    }
  }
}
