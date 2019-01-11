package com.wardziniak.aviation.analyzer.app

import com.wardziniak.aviation.analyzer.app.PocStructureStreamKafkaApp.Person
import com.wardziniak.aviation.api.model.AnalyticFlightSnapshot
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.mllib.regression.StreamingLinearRegressionWithSGD
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

class LandingAnalyzerApp extends App {


  import org.apache.spark.sql.functions._

  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


  import session.implicits._
  val df: DataFrame = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topicA")
    .load()


  df.writeStream.trigger(???)

//  val lr = new LinearRegression()
//    .setMaxIter(10)
//    .setRegParam(0.3)
//    .setElasticNetParam(0.8)
//
//  // Fit the model
//  val lrModel = lr.fit(training)
//
//  val model = new StreamingLinearRegressionWithSGD()
//    .setInitialWeights(???)
//
//  model.trainOn(trainingData)
//
//  df.writeStream.foreachBatch()


  val schema = ScalaReflection.schemaFor[AnalyticFlightSnapshot].dataType.asInstanceOf[StructType]

}
