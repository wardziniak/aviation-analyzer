package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.ProcessorContext

object SingleFlightWindow {


  case class SingleFlightWindowTransformation() extends AbstractTransformer[String, FlightSnapshot, KeyValue[String, FlightSnapshot]] {
    override def init(context: ProcessorContext): Unit = {

    }

    override def transform(key: String, value: FlightSnapshot): KeyValue[String, FlightSnapshot] = {
      ???
    }
  }

}
