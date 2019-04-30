package com.wardziniak.aviation.processing.internal

import java.time.Duration

import com.wardziniak.aviation.api.model.FlightSnapshot.FlightNumberIata
import com.wardziniak.aviation.api.model.{FlightSnapshot, InAirFlightData}
import org.apache.kafka.streams.processor.AbstractProcessor

object Normalization {

  case class NormalizationProcessor(windowsSize: Duration) extends AbstractProcessor[FlightNumberIata, InAirFlightData] {
    override def process(flightNumberIata: FlightNumberIata, inAirFlightData: InAirFlightData): Unit = {
      val snapshots = inAirFlightData.flightInfo

      // windowsSize - 3
      // 1, 3, 5, 10, 12, 15, 17, 21, 29
      //

      // duplication of each elements, than remove first and group them into pairs
      snapshots
        .flatMap(snapshot => List(snapshot, snapshot)) // Duplicate each element
        .tail // remove duplication of first elements
        .grouped(2)
        .map(group => (group.head, group.tail.head))
        .filter(f => windowsSize.minus(Duration.ofSeconds(f._2.updated - f._1.updated)).isNegative) // filtered out only pair with long break
        .flatMap(f => Seq(f._1, calculateMidSnapshot(f._1, f._2), f._2))
        .toList.sortBy(_.updated)
      //        .foreach(snapshot => context().forward(flightNumberIata, snapshot))
    }

    private def calculateMidSnapshot(begin: FlightSnapshot, end: FlightSnapshot): FlightSnapshot = ???
  }

}
