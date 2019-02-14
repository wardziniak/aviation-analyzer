package com.wardziniak.aviation.preprocessing.app

import java.io.ByteArrayInputStream

import com.sksamuel.avro4s.AvroInputStream
import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.common.serialization.GenericDeserializer
import com.wardziniak.aviation.preprocessing.PreProcessingTopologyBuilder
import com.wardziniak.aviation.preprocessing.app.PreProcessingApp.{buildTopology, runStreamWithConfiguration}
import com.wardziniak.aviation.preprocessing.utils.StreamApp
import com.wardziniak.aviation.preprocessing.utils.kafka.StreamConfigBuilder
import org.apache.kafka.streams.scala.kstream.{Consumed, Joined}
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}

object TestApp
  extends App
    //with StreamApp
    with LazyLogging {

  val TimeDifferenceMs = 300000

  val preProcessingAppConfig = StreamConfigBuilder()
    .withApplicationId(applicationId = "testApp")
    .withBootstrapServer(bootstrapServer = "localhost:9092").build


  val builder = new StreamsBuilder()

  val in1 = builder.stream("input1")(Consumed.`with`(Serdes.String, Serdes.String))
  val in2 = builder.table("input2")(Consumed.`with`(Serdes.String, Serdes.String))
  in1.join(in2)((v1, v2) => v1)(Joined.`with`(Serdes.String, Serdes.String, Serdes.String))

  println(builder.build().describe())

  //runStreamWithConfiguration(topology = applicationTopology, streamProperties = preProcessingAppConfig)


  import com.wardziniak.aviation.api.model.InAirFlightData
  //val deserializer = new GenericDeserializer[InAirFlightData]()

  //val aa = deserializer.deserialize("", inAirFlightData.getBytes)

//
//  val in = new ByteArrayInputStream(inAirFlightData.getBytes)
//  val input = AvroInputStream.json[InAirFlightData](in)
//  //input.iterator.toList.head
//  val dd = input.singleEntity
//  //input.singleEntity.get
//  dd.get


}
