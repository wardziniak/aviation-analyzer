package com.wardziniak.aviation.processing.internal

import org.apache.kafka.streams.kstream.Transformer

trait AbstractTransformer[K, V, R] extends Transformer[K, V, R] {
  override def close(): Unit = {}
}
