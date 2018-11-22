package com.wardziniak.aviation.utils

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.analyzer.Topics.RawInputTopic
import com.wardziniak.aviation.analyzer.kafka.StreamConfigBuilder
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericSerde
//import com.wardziniak.aviation.preprocessing.PreProcessingApp.{builder, preProcessingAppConfig, withStreamApp}
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{Consumed, Produced}
import org.apache.kafka.streams.scala.StreamsBuilder

object FilterSomeFlight
  extends App
    with LazyLogging {


  val preProcessingAppConfig = StreamConfigBuilder()
    .withApplicationId(applicationId = "filterSomeFLigt3")
    .withBootstrapServer(bootstrapServer = "192.168.1.17:9092").build


//  val builder: StreamsBuilder = new StreamsBuilder()
//  withStreamApp(builder, preProcessingAppConfig) {
//    val source = builder.stream(RawInputTopic)(Consumed.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
//    source
//      .filter((_, flight) => flight.flightNumber.iata == "LH1208")
//      .peek((_, flight) => logger.info(s"$flight"))
//      .to("lh1208")(Produced.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
//  }

}
