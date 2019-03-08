package com.wardziniak.aviation.processing.internal

import java.time.Duration

import org.apache.kafka.streams.processor.{AbstractProcessor, ProcessorContext, PunctuationType}
import org.apache.kafka.streams.state.KeyValueStore

object ProcessorHelper {

  abstract class ProcessorWithStateStore[Key, Value, Agg](stateStoreName: String) extends AbstractProcessor[Key, Value] {

    protected var stateStore: KeyValueStore[Key, Agg] = _

    override def init(context: ProcessorContext): Unit = {
      super.init(context)
      stateStore = context.getStateStore(stateStoreName).asInstanceOf[KeyValueStore[Key, Agg]]
    }
  }

  abstract class ProcessorWithStateStoreAndPunctuator[Key, Value, Agg](stateStoreName: String)
    extends ProcessorWithStateStore[Key, Value, Agg](stateStoreName) {
    val punctuatorInterval: Duration

    override def init(context: ProcessorContext): Unit = {
      super.init(context)
      context.schedule(punctuatorInterval, PunctuationType.WALL_CLOCK_TIME, punctuate)
    }

    def punctuate(currentTimestamp: Long): Unit
  }

}
