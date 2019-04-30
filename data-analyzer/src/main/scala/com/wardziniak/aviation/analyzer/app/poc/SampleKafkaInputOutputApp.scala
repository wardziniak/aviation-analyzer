package com.wardziniak.aviation.analyzer.app.poc

import com.wardziniak.aviation.analyzer.app.PocStructureStreamKafkaApp.df
import com.wardziniak.aviation.analyzer.app.poc.PocKafkaAllFieldStructureStreaming.allFields
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{concat, lit}

object SampleKafkaInputOutputApp extends App {


  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


  import session.implicits._
  val df: DataFrame = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("startingOffsets", "earliest")
    .option("subscribe", "input")
    .load()
    .selectExpr(s"CAST(value AS STRING) AS field1",  s"CAST(key AS STRING) AS field2")
    .select(concat($"field1", lit(" "), $"field2").alias("value"))


  //df.selectExpr(s"CAST(value AS STRING) AS field1, CAST(value AS STRING) AS field2")



  val sQuery: StreamingQuery = df.writeStream
    .trigger(Trigger.ProcessingTime("5 second"))
    .option("checkpointLocation", "/tmp/checkpoint/")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("topic", "output")
    .format("kafka")
    .start("/tmp/streamBar")


  sQuery.awaitTermination

}
