package com.wardziniak.aviation.analyzer.app

import org.apache.spark.sql.SparkSession

object SampleAgg extends App {


  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()
  val inputDf = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "input")
    .option("startingOffsets", """{"input":{"0":0}""")
    .option("endingOffsets", """{"input":{"0":1}""")
    .load()



  inputDf.writeStream
    .outputMode("append")
    .format("console")
    .option("checkpointLocation", "/tmp/checkpoint/")
    .start()
    .awaitTermination()
}
