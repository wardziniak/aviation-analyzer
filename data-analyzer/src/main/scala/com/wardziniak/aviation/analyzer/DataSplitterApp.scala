package com.wardziniak.aviation.analyzer

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.analyzer
import com.wardziniak.aviation.analyzer.InputTopicMErgerApp.properties
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import com.wardziniak.aviation.common.serialization.{GenericSerde, GenericSerializer}
import com.wardziniak.aviation.preprocessing.InAirTransformer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.kstream.{Consumed, Produced}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores => KStore}

import scala.concurrent.Future

object DataSplitterApp extends App with LazyLogging {

//  val flightInAirStore: String = "flights-in-air-store1"
//  val rawInputTopic = "aviation-in"
//  val mainInputTopic = "single-part-in"
//  val outTopic = "output"

  // Application should split data accross different topic based on key

  // Analiza tylko dla lotu n.p.

  // rawData.filter(isProperRecord).filter(onGround).groupBy(numerLotu, czas).widnow(60min).reduk

  // rawData.filter(isProperRecord).filter(onGround).a

  // rawData.filter(isProperRecord).filter(inAir).join(firstOnGround).map(calculateTimeToDestination)


//  val builder: StreamsBuilder = new StreamsBuilder()
//  val source = builder.stream(analyzer.Topics.MainInputTopic)(Consumed.`with`(Serdes.String(), GenericSerde[FlightSnapshot]()))
//
//  val flightsInAirStore: StoreBuilder[KeyValueStore[String,InAirFlightData]] =
//    KStore.keyValueStoreBuilder(KStore.persistentKeyValueStore(analyzer.Stores.InAirFlightStoreName), Serdes.String(), GenericSerde[InAirFlightData]())
//  builder.addStateStore(flightsInAirStore)
//  source.filter((_, flightInfo) => flightInfo.airlineCode.icao == "LOT")
//    .filter((_, flightInfo) => !flightInfo.speed.isGround)
//    .transform[String, FlightSnapshot](InAirTransformer(analyzer.Stores.InAirFlightStoreName), analyzer.Stores.InAirFlightStoreName)
//      .to(analyzer.Topics.InAirAfterLandedTopic)(Produced.`with`(Serdes.String(), new GenericSerde[FlightSnapshot]))
//
//
//  val properties = new Properties()
//  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "loo2")
//  properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.17:9092")
//  properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
//  properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
//  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//  val streams: KafkaStreams = new KafkaStreams(builder.build(), properties)
//  streams.setUncaughtExceptionHandler((t: Thread, e: Throwable) => {
//    logger.error("Uncaught exception in kafka stream", e)
//  })
//
//  Runtime.getRuntime.addShutdownHook(new Thread(() => {
//    logger.info("Closing stream")
//    streams.close(10, TimeUnit.SECONDS)
//    logger.info("Stream closed")
//  }))
//
//  streams.start()

//  source.filter((_, flightInfo) => flightInfo.speed.isGround).process(???)
//  val onGround = source.filter((_, flightInfo) => flightInfo.speed.isGround).transform(???)

}