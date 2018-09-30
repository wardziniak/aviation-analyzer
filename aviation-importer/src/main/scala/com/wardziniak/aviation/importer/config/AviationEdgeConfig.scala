package com.wardziniak.aviation.importer.config

case class AviationEdgeConfig(kafka: KafkaConfig, serverKey: String)

case class KafkaConfig(server: String, mainTopic: String)