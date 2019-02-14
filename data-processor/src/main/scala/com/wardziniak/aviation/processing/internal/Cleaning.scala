package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.{ProcessorContext, Punctuator}

/**
  * Punctuator with cleaning should take few snapshot for same flight and try to figure out if all are ok
  */

object Cleaning {

  case class CleaningTransformer() extends Transformer[String, FlightSnapshot, KeyValue[String, FlightSnapshot]] {
    override def init(context: ProcessorContext): Unit = {
    }

    override def transform(key: String, value: FlightSnapshot): KeyValue[String, FlightSnapshot] = {
      new KeyValue[String, FlightSnapshot](key, value)
    }

    override def close(): Unit = {
    }
  }


  /**
    * Cleaning Algorithm for particular flight:
    * 1. If first few snapshot and are not old do nothing, wait for more
    * 2. If few snapshot but old, drop them
    * 3. If there is lot of snapshots remove those, that are not match to others (ex. change airport, locations is not in pattern with others)
    * 4. If there is lot of snapshots some can be passed forward
    * 5. If there is lot of snapshot and there are old, pass them forward
    */
  case class CleaningPunctuator() extends Punctuator {
    override def punctuate(timestamp: Long): Unit = {
    }
  }
}
