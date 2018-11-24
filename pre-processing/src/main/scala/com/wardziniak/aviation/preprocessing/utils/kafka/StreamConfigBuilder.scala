package com.wardziniak.aviation.preprocessing.utils.kafka

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig

case class StreamConfigBuilder(
  applicationId: String = "applicationId",
  bootstrapServer: String = "localhost:9092",
  defaultKeySerde: AnyRef = Serdes.String().getClass,
  defaultValueSerde: AnyRef = Serdes.String().getClass,
  autoOffsetReset: String = "latest") {

  def withApplicationId(applicationId: String): StreamConfigBuilder = {
    this.copy(applicationId = applicationId)
  }

  def withBootstrapServer(bootstrapServer: String): StreamConfigBuilder = {
    this.copy(bootstrapServer = bootstrapServer)
  }

  def withAutoOffsetReset(autoOffsetReset: String): StreamConfigBuilder = {
    this.copy(autoOffsetReset = autoOffsetReset)
  }

  def build: Properties = {
    val properties = new Properties()

    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId)
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, defaultKeySerde)
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, defaultValueSerde)
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)
//    properties.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, int2Integer(1))
    properties
  }

}
