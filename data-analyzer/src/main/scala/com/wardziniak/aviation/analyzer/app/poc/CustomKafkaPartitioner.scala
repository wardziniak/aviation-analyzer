package com.wardziniak.aviation.analyzer.app.poc

import java.util

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster

class CustomKafkaPartitioner extends Partitioner {
  override def partition(topic: String, key: scala.Any, keyBytes: Array[Byte], value: scala.Any, valueBytes: Array[Byte], cluster: Cluster): Int = {
    0
  }

  override def close(): Unit = {}

  override def configure(configs: util.Map[String, _]): Unit = {}
}
