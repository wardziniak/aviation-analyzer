package com.wardziniak.aviation.processing.internal

import com.wardziniak.aviation.api.model.FlightSnapshot
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore

object Deduplication {

  case class DeduplicationTransformer(deduplicationStoreName: String) extends AbstractTransformer[String, FlightSnapshot, KeyValue[String, FlightSnapshot]] {

    var deduplicationStore: KeyValueStore[String, FlightSnapshot] = _

    override def init(context: ProcessorContext): Unit = {
      deduplicationStore = context.getStateStore(deduplicationStoreName).asInstanceOf[KeyValueStore[String, FlightSnapshot]]
      // TODO: Add Punctuator to pass "very old" snapshot (last snapshot - now will trigger it) - no new is possible.
    }

    override def transform(flightNumberIata: String, currentSnapshot: FlightSnapshot): KeyValue[String, FlightSnapshot] = {
      val lastSnapshot = deduplicationStore.get(flightNumberIata)
      deduplicationStore.put(flightNumberIata, currentSnapshot)
      // same current time, so we can skip lastSnapshot (deduplicate)
      if (lastSnapshot.updated == currentSnapshot.updated) null
      else new KeyValue[String, FlightSnapshot](flightNumberIata, lastSnapshot)
    }
  }

}
