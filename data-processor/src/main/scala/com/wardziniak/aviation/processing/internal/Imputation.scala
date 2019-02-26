package com.wardziniak.aviation.processing.internal

import java.time.Duration

import com.wardziniak.aviation.api.model.FlightSnapshot
import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import org.apache.kafka.streams.kstream.ValueTransformerWithKey
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore

object Imputation {

  val Interval: Duration = ???

  case class ImputationTransformer(windowsSize: Duration)
    extends ValueTransformerWithKey[FlightNumberIata, FlightSnapshot, FlightSnapshot] {

    var flightSnapshotStore: KeyValueStore[FlightNumberIata, FlightSnapshot] = _
    var context: ProcessorContext = _

    override def init(context: ProcessorContext): Unit = {
      this.context = context
    }

    override def close(): Unit = {}

    override def transform(flightNumberIata: FlightNumberIata, flightSnapshot: FlightSnapshot): FlightSnapshot = {
      val previousSnapshot = flightSnapshotStore.get(flightNumberIata)
      if (previousSnapshot != null) {
        // TODO make calculation more generic
        val timeDiff = Duration.ofSeconds(flightSnapshot.updated).minus(Duration.ofSeconds(previousSnapshot.updated))
        if (!timeDiff.isNegative) {
          val numberOfPointsToImput = (timeDiff.toMillis / Interval.toMillis).toInt
          List.range(0, numberOfPointsToImput).map(it => {
            FlightSnapshot(
              localization = previousSnapshot.localization.getNthLocation(numberOfPointsToImput, it, flightSnapshot.localization),
              speed = previousSnapshot.speed.getNthLocation(numberOfPointsToImput, it, flightSnapshot.speed),
              departure = flightSnapshot.departure,
              arrival = flightSnapshot.arrival,
              aircraft = flightSnapshot.aircraft,
              flightNumber = flightSnapshot.flightNumber,
              airlineCode = flightSnapshot.airlineCode,
              enRoute = flightSnapshot.enRoute,
              updated = previousSnapshot.updated + (flightSnapshot.updated - previousSnapshot.updated) / numberOfPointsToImput * it
            )
          }).foreach(snapshot => context.forward(flightNumberIata, snapshot))
        }
      }
      flightSnapshotStore.put(flightNumberIata, flightSnapshot)
      null
    }
  }
}
