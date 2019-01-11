package com.wardziniak.aviation.preprocessing

import java.util.{Objects, Properties}

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.kstream.{Consumed, TimeWindows}
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}

object SampleAggregation extends App {











  val properties = new Properties();

  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "applicationId1")
  properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String)
  properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String)
  properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")


  val builder = new StreamsBuilder()

//  builder
//    .stream("sessionTopic")(Consumed.`with`(Serdes.String, Serdes.String))
//    .peek((a,b) =>println(s"before group:$a, $b"))
//    .groupByKey.windowedBy(TimeWindows.of(5000))
//    .reduce((v1, v2) => if ("DONE".equals(v1)  v1 else v2)
//    .suppress(Suppressed.untilWindowCloses(unbounded()))
//    .filter((k,v)-> Objects.nonNull(v) && v.equals("RUNNING"))
//    .toStream().peek((a,b)->System.out.println("This Value is missing: \n   "+a.toString()+b));
  //                .windowedBy(SessionWindows.with(SESSION_DURATION))
  //                .reduce(new SessionReducer())
  //                .toStream((windowed, value) -> windowed.key())
  //                .filter((k,v)-> Objects.nonNull(v) && v.getStatus() == Status.RUNNING)
  //                .peek((a,b)->System.out.println("This Value is missing: \n   "+a.toString()+b.toString()));


//  val streams = new KafkaStreams(builder.build(), properties)
//  streams.start()
//  Runtime.getRuntime().addShutdownHook(new Thread(streams::close))

}
