package com.wardziniak.aviation.analyzer.app.kafka

import org.apache.spark.sql.SparkSession

object SampleKafkaProcess extends App {

  val session: SparkSession = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()
  val inputDf = session.read
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "input")
    .option("startingOffsets", """{"input":{"0":0}}""")
    .option("endingOffsets", """{"input":{"0":5}}""")
    .load()



  inputDf.show()
}
