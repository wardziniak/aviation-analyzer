package com.wardziniak.aviation.analyzer.app

import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.json4s.Formats
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import scala.reflect.Manifest


object StructureStreamApp extends App {

  case class Person(name: String, age: Int)

  import org.apache.spark.sql.functions._
  //val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")

  // .set("spark.driver.host", "localhost")

  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


  import session.implicits._
  val df: DataFrame = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topicA")
    .load()

  val schema = ScalaReflection.schemaFor[Person].dataType.asInstanceOf[StructType]

//  val schema = new StructType()
//    .add($"name".string)
//    .add($"age".int)

//  df.printSchema()
////  val r1 = df.select($"value" cast "string" as "json")
//  val r1 = df.selectExpr("CAST(value AS STRING)").alias("value")
//  r1.printSchema()
//  val r11 = r1.select(from_json($"value", schema) as "data")
//  r11.printSchema()
//  val r12 = r11.select("data.*")
//  r12.printSchema()

//  import org.apache.spark.sql.Encoders
//  implicit val statisticsRecordEncoder = Encoders.product[StatisticsRecord]
//  val myDeserializerUDF = udf { bytes => deserialize("hello", bytes) }
//  df.select(myDeserializerUDF($"value") as "value_des")

  df.printSchema()
  val r1 = df.select($"value" cast "string" as "json")
    .select(from_json($"json", schema) as "data")
    .select("data.*").as[Person].map(p => p.copy(age = p.age+4)).toDF().toJSON
  r1.printSchema()
  val r11 =   r1.selectExpr("CAST(value AS STRING)")//r1.select($"value" cast "string")

  //val r1 = df.selectExpr("CAST(value AS STRING)").map(_.getString(0))//.as[String]
  val r2 = r11
  r2.printSchema()
  val results = r2//r1.map(ff => Serialization.read[Person](ff))//r1.map(_.getString(0)).as[Person]
  results.printSchema()
//  results.show()

//  df.select()




  val d: StreamingQuery = results.writeStream.trigger(Trigger.ProcessingTime("5 second"))
    .option("checkpointLocation", "/tmp/checkpoint/")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("topic", "topicAOut")
    .format("kafka")
    .start("/tmp/streamBar")

  d.awaitTermination(30000)



//  val lines = session.readStream
//    .format("socket")
//    .option("host", "localhost")
//    .option("port", 9999)
//    .load()
//
//  // Split the lines into words
//  val words: Dataset[String] = lines.as[String].flatMap(_.split(" "))
//
//  // Generate running word count
//  val wordCounts = words.groupBy("value").count()
}


/**
  * https://stackoverflow.com/questions/42506801/how-to-use-from-json-with-kafka-connect-0-10-and-spark-structured-streaming
  */