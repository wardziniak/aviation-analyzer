package com.wardziniak.aviation.analyzer

import java.util

//import com.holdenkarau.spark.testing.{SharedSparkContext, StreamingSuiteBase, StructuredStreamingBase}
import com.typesafe.scalalogging.LazyLogging
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.scalatest.{BeforeAndAfterAll, FunSuite}

class StreamingKafkaSpec {
//  extends FunSuite
//    with SharedSparkContext
//    with BeforeAndAfterAll
//    with EmbeddedKafka
//    with StreamingSuiteBase
//    with LazyLogging {

//  implicit lazy val kafkaConfig: EmbeddedKafkaConfig = EmbeddedKafkaConfig()
//
//
//  override def beforeAll(): Unit = {
//    EmbeddedKafka.start()
//    EmbeddedKafka.createCustomTopic("topicA")
//    EmbeddedKafka.publishStringMessageToKafka("topicA", "DUUUPAAAAAAA fdsaf ekqlfe wf")
//    super.beforeAll()
//  }
//
//  override def afterAll(): Unit = {
//    super.afterAll()
//    EmbeddedKafka.stop()
//  }
//
//
//  test("count words") {
//
//    def count(lines: DStream[String]): DStream[(String, Int)] =
//      lines.flatMap(_.split(" "))
//        .map(word => (word, 1))
//        .reduceByKey(_ + _)
//
//    val input = List(List("the word the"))
//    val expected = List(List(("the", 2), ("word", 1)))
//    testOperation[String, (String, Int)](input, count _ , expected, ordered = false)
//  }
//
//  test("fdsaa") {
//
//    logger.error("STARRTP")
//
//    //val sparkConf = new SparkConf().setAppName("SampleApp")
//    val ssc = new StreamingContext(sc, Seconds(2))
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
//      "bootstrap.servers" -> "localhost:6001",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "use_a_separate_group_id_for_each_stream",
//      "auto.offset.reset" -> "earliest",
//      "enable.auto.commit" -> (false: java.lang.Boolean)
//    )
//
//    val topics = Array("topicA")
//    val stream = KafkaUtils.createDirectStream[String, String](
//      ssc,
//      PreferConsistent,
//      Subscribe[String, String](topics, kafkaParams)
//    )
//
//    //Thread.sleep(10000)
//
//    logger.error("DUUUUU")
//
//    val dd = stream.map(_.value())
//
//    EmbeddedKafka.publishStringMessageToKafka("topicA", "DUUUPAAAAAAA jaja ekqlfe wf")
//
//
//    dd.saveAsTextFiles("/tmp/bwar", "bwar")
//
//    dd.foreachRDD(pp => logger.error(s"FFFFF"))
//
//    logger.error("POOO")
//
//    //dd.print()
//
//    ssc.start()
//    ssc.awaitTerminationOrTimeout(5000)
//
//
//
//  }

//  test("test initializing spark context") {
//
////    val topic = "test-topic"
////    val message = "HelloWorld! Hej"
////    kafkaTestUtils.createTopic(topic)
////    kafkaTestUtils.sendMessages(topic, message.getBytes)
////    kafkaTestUtils.sendMessages(topic, message.getBytes)
//
//
//
////    val ssc = new StreamingContext(sc, Seconds(2))
////
////    val kafkaParams = Map[String, Object](
////      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
////      ConsumerConfig.GROUP_ID_CONFIG -> "groupId",
////      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
////      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])
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
//      "enable.auto.commit" -> (false: java.lang.Boolean)
//    )
//
//    val ssc = new StreamingContext(sc, Seconds(2))
//    val topics = Array("topicA", "topicB")
//    val stream = KafkaUtils.createDirectStream[String, String](
//      ssc,
//      PreferConsistent,
//      Subscribe[String, String](topics, kafkaParams)
//    )
//
//    stream.map(record => (record.key, record.value))
//
////    val sparkConf = new SparkConf().setAppName("SampleApp")
////    val ssc = new StreamingContext(sparkConf, Seconds(2))
//
////    val kafkaParams = Map[String, Object](
////      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
////      ConsumerConfig.GROUP_ID_CONFIG -> "groupId",
////      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
////      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])
////
////
////    val messages = KafkaUtils.createDirectStream[String, String](
////      ssc,
////      LocationStrategies.PreferConsistent,
////      ConsumerStrategies.Subscribe[String, String](Set("topic1"), kafkaParams))
////
////    val lines = messages.map(_.value)
////    val words = lines.flatMap(_.split(" "))
////    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
////    wordCounts.print()
//
//    ssc.start()
//
//    //Thread.sleep(10000)
//    //ssc.awaitTermination()
//    ssc.stop()
//  }
}
