package com.wardziniak.aviation.analyzer.app.poc

import com.wardziniak.aviation.analyzer.app.PocStructureStreamKafkaApp.Person
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

object PocKafkaAllFieldStructureStreaming extends App {


  import org.apache.spark.sql.functions._


  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


  import session.implicits._
  val df: DataFrame = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topicIn")
    .load()

  val schema = ScalaReflection.schemaFor[Person].dataType.asInstanceOf[StructType]

  val allFields = df.selectExpr(
    s"CAST(value AS STRING) AS csv",
  s"CAST(key AS STRING) AS key",
    s"topic as topic",
    s"partition as partition",
    s"offset as offset",
    s"timestamp as timestamp",
    s"timestampType as timestampType"
  )
    .select(concat(
      $"key", lit(":"),
    $"value", lit(":"),
    $"topic", lit(":"),
    $"offset", lit(":"),
    $"timestamp", lit(":"),
    $"timestampType", lit(":")
    ).alias("value"))


//  val results = df.select(s"CAST(value AS STRING) AS value")
//    .select(from_json($"json", schema) as "data")
//    .select("data.*").as[Person].map(p => p.copy(age = p.age+7)).toDF().toJSON
//    .selectExpr("CAST(value AS STRING)")
//  results.printSchema()


  allFields.printSchema()



  val sQuery: StreamingQuery = allFields.writeStream.trigger(Trigger.ProcessingTime("5 second"))
    .option("checkpointLocation", "/tmp/checkpoint/")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("topic", "topicOut")
    .format("kafka")
    .start("/tmp/streamBar")



  sQuery.awaitTermination(30000)

}
