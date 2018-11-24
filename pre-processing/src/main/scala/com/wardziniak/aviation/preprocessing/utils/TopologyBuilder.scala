package com.wardziniak.aviation.preprocessing.utils

import org.apache.kafka.streams.Topology

trait TopologyBuilder {

  def buildTopology: Topology
}
