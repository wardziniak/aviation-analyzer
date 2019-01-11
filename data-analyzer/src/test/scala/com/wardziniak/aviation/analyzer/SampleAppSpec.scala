package com.wardziniak.aviation.analyzer

import java.nio.file.Files

import com.typesafe.scalalogging.LazyLogging
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.scalatest.{BeforeAndAfterAll, FunSuite}

//import com.holdenkarau.spark.testing.SharedSparkContext
//import org.scalatest.FunSuite

class SampleAppSpec
//  extends FunSuite
//    with BeforeAndAfterAll
    extends EmbeddedKafka
    with LazyLogging {

//  private val master = "local[2]"
//  private val appName = "example-spark-streaming"
//  private val batchDuration = Seconds(1)
//  private val checkpointDir = Files.createTempDirectory(appName).toString
//
//  private var sc: SparkContext = _
//  private var ssc: StreamingContext = _
//
//  test("dfasf") {
//
//    import org.apache.spark.sql.SparkSession
//
//    val conf = new SparkConf()
//      .setMaster(master)
//      .setAppName(appName)
//
//    sc = SparkContext.getOrCreate(conf)
//
////    val sparkSession = SparkSession
////      .builder
////      .appName("WorldBankIndex")
////    .getOrCreate()
//
//    val ssc = new StreamingContext(sc, Seconds(5))
//
//    //val stream = ssc.textFileStream("hdfs:///tmp/file.txt")
//
//    import collection.JavaConverters._
//
//    import org.apache.kafka.clients.consumer.ConsumerRecord
//    import org.apache.kafka.common.serialization.StringDeserializer
//    import org.apache.spark.streaming.kafka010._
//    import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
//    import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
//
//    val kafkaParams = Map[String, Object](
//      "bootstrap.servers" -> "localhost:9092",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "use_a_separate_group_id_for_each_stream",
//      "auto.offset.reset" -> "latest",
//      "enable.auto.commit" -> (true: java.lang.Boolean)
//    )
//
//    val topics = Array("topicA")
//    val stream = KafkaUtils.createDirectStream[String, String](
//      ssc,
//      PreferConsistent,
//      Subscribe[String, String](topics, kafkaParams)
//    )
//
//
//
//    //Thread.sleep(10000)
//
//    logger.error("DUUUUU")
//
//    //val dd = stream.map(_.value())
//
//    //EmbeddedKafka.publishStringMessageToKafka("topicA", "DUUUPAAAAAAA jaja ekqlfe wf")
//
//
//    ///stream.repartition(1).saveAsTextFiles("/tmp/bar", "bwar")
//
//    //stream.map(_.value().toString).
//
//    stream.map(_.value().toString).saveAsTextFiles("/tmp/bar", "bwar")
//
//    //stream.map(_.value()).flatMap(_.split(" "))foreachRDD(v => logger.error(s"FFFFF:${v}"))
//
//    //stream.print()
//
//    logger.error("POOO")
//
//    //stream.c
//
//    //dd.print()
//
//    ssc.start()
//
//    //ssc.awaitTermination()
//    ssc.awaitTerminationOrTimeout(10000)
//
//
////    val list = List(1, 2, 3, 4)
////      val rdd = sc.parallelize(list)
////
////      assert(rdd.count === list.length)
//
//  }

}
