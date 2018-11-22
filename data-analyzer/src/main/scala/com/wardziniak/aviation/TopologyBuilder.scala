package com.wardziniak.aviation

import org.apache.kafka.streams.Topology

trait TopologyBuilder {

  def buildTopology: Topology
}
