package com.wardziniak.aviation.app

import com.typesafe.scalalogging.LazyLogging
import com.wardziniak.aviation.StreamApp
import com.wardziniak.aviation.analyzer.Stores.{InAirFlightStoreName, LandedFlightStoreName}
import com.wardziniak.aviation.analyzer.kafka.StreamConfigBuilder
import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.common.serialization.GenericSerde
import com.wardziniak.aviation.preprocessing.LandingTransformer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.kstream.{Consumed, Materialized, Produced}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.state.{KeyValueStore, StoreBuilder, Stores}

object TestStoreApp extends App
//  with StreamApp
  with LazyLogging {

  case class Person(id: Long, name: String, lastName: String)

  val storeName = "storeName"
  val notificationTopic = "notificationTopic"
  val peopleResultTopic = "peopleResult"

  val appProperties = StreamConfigBuilder()
    .withApplicationId(applicationId = "test-store1")
    .withBootstrapServer(bootstrapServer = "localhost:9092").build

  val builder: StreamsBuilder = new StreamsBuilder()

//  withStreamApp(builder, appProperties) {
//    val tempStoreBuilder: StoreBuilder[KeyValueStore[String,String]] =
//      Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(InAirFlightStoreName), Serdes.String(), Serdes.String())
//
//    tempStoreBuilder.build()
//
//    val mal1: Materialized[String, String, KeyValueStore[String,String]] = Materialized.as[String, String, KeyValueStore[String,String]](storeName)
//
//
//    val mal: Materialized[String, Person, KeyValueStore[Bytes, Array[Byte]]] = Materialized.as[String, Person, KeyValueStore[Bytes, Array[Byte]]](storeName)
//    val ktable = builder.table("tableTopicName", mal)(Consumed.`with`(Serdes.String(), new GenericSerde[Person]))
//
//    builder.stream(notificationTopic)(Consumed.`with`(Serdes.String(), Serdes.String()))
//        .transform[String, String](TestTransformer(storeName), storeName)
//        .foreach((key, value) => logger.info(s"chyba null [$key, $value]"))
//
//    ktable.toStream
//      .peek((key, value) => logger.info(s"[ktable = key=$key, value=$value]"))
//      .to(peopleResultTopic)(Produced.`with`(Serdes.String(), new GenericSerde[Person]))
//
//  }

}
