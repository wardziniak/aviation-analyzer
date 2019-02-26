package com.wardziniak.aviation.analyzer.app

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object SampleSparkStreamingOffsetApp extends App {

  import org.apache.kafka.common.serialization.StringDeserializer
  import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
  import org.apache.spark.streaming.kafka010._

//  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
//  val ssc = new StreamingContext(conf, Seconds(5))

  import scala.collection.JavaConverters._

  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


//  val offsetRanges = Array(
//    // topic, partition, inclusive starting offset, exclusive ending offset
//    OffsetRange("input", 0, 2, 3)
//  )
//
//  val topics = Array("input")
  // Import dependencies and create kafka params as in Create Direct Stream above


  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "groupId"
  )
  val offsetRanges = Array(
    OffsetRange("input", 0, 2, 4) // <-- topic name, partition number, fromOffset, untilOffset
  )

  val sparkContext: SparkContext = ???
  val rdd = KafkaUtils.createRDD(sparkContext, kafkaParams.asJava, offsetRanges, PreferConsistent)


  rdd.map(record => (record.key, record.value)).take(10).foreach(println)
//  stream.map(record => (record.key, record.value)).print()
//
//  ssc.start()             // Start the computation
//  ssc.awaitTermination()
}
