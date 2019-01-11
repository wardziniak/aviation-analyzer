package com.wardziniak.aviation.preprocessing

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.{Consumed, Materialized, Predicate}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.state.KeyValueStore

trait TestTopologyBuilder {


  def buildTopology: Topology = {
    val builder: StreamsBuilder = new StreamsBuilder()

//    builder
//      .stream("topic", Consumed.with(serdeKeySessionEvent, serdeValueSessionEvent))
//    .groupByKey(Grouped.with(serdeKeySessionEvent, serdeValueSessionEvent))
//    .windowedBy(SessionWindows.with(SESSION_DURATION))
//    .reduce(new SessionReducer())
//      .toStream((windowed, value) -> windowed.key())
//      .filter((k,v)-> Objects.nonNull(v) && v.getStatus() == Status.RUNNING)
//      .peek((a,b)->System.out.println("This Value is missing: \n   "+a.toString()+b.toString()));


    builder.build()
  }
}
