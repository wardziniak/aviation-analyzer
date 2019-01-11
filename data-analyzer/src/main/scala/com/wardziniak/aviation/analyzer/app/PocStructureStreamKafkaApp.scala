package com.wardziniak.aviation.analyzer.app

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

object PocStructureStreamKafkaApp extends App {

  case class Person(name: String, age: Int)

  import org.apache.spark.sql.functions._

  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


  import session.implicits._
  val df: DataFrame = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topicA")
    .load()

  val schema = ScalaReflection.schemaFor[Person].dataType.asInstanceOf[StructType]


  val results = df.selectExpr(s"CAST(value AS STRING) AS json")
    .select(from_json($"json", schema) as "data")
    .select("data.*").as[Person].map(p => p.copy(age = p.age+7)).toDF().toJSON
    .selectExpr("CAST(value AS STRING)")
  results.printSchema()




  val sQuery: StreamingQuery = results.writeStream.trigger(Trigger.ProcessingTime("5 second"))
    .option("checkpointLocation", "/tmp/checkpoint/")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("topic", "topicAOut")
    .format("kafka")
    .start("/tmp/streamBar")



  sQuery.awaitTermination(30000)




}
