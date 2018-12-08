package com.wardziniak.aviation.analyzer.app

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SocketStreamApp extends App {


  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val ssc = new StreamingContext(conf, Seconds(1))
  val lines = ssc.socketTextStream("localhost", 9999)

  val words = lines.flatMap(_.split(" "))

  import org.apache.spark.streaming.StreamingContext._ // not necessary since Spark 1.3
  // Count each word in each batch
  val pairs = words.map(word => (word, 1))
  val wordCounts = pairs.reduceByKey(_ + _)

  // Print the first ten elements of each RDD generated in this DStream to the console
  wordCounts.print()

  ssc.start()             // Start the computation
  ssc.awaitTerminationOrTimeout(20000)  // Wait for the computation to terminate
}
