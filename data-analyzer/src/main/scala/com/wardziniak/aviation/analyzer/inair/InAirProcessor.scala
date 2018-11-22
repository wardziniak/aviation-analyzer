package com.wardziniak.aviation.analyzer.inair

import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import org.apache.kafka.streams.processor.{Processor, ProcessorContext}
import org.apache.kafka.streams.state.KeyValueStore

case class InAirProcessor(flightInAirStore: String)
  extends Processor[String, FlightSnapshot] {

  var state: KeyValueStore[String, InAirFlightData] = _

  override def init(context: ProcessorContext): Unit = {
    state = context.getStateStore(flightInAirStore).asInstanceOf[KeyValueStore[String, InAirFlightData]]
    context.topic()
  }

  override def process(icao: String, flightSnapshot: FlightSnapshot): Unit = {
    Option(state.get(icao))
      .map(_.addSnapshot(flightSnapshot))
      .orElse(Some(InAirFlightData(flightSnapshot)))
      .foreach(state.put(icao, _))
  }

  override def close(): Unit = {}
}
