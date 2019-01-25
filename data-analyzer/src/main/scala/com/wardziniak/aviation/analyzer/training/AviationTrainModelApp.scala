package com.wardziniak.aviation.analyzer.training

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.rdd.MongoRDD
import com.wardziniak.aviation.analyzer.app.StructureStreamApp.Person
import com.wardziniak.aviation.api.model.AnalyticFlightSnapshot
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.bson.Document

object AviationTrainModelApp extends App {

  val mongodbUri = "mongodb://127.0.0.1/test.landed"


  import org.apache.spark.sql.SparkSession

  val analyticSchema = ScalaReflection.schemaFor[AnalyticFlightSnapshot].dataType.asInstanceOf[StructType]

  val spark = SparkSession.builder()
    .master("local")
    .appName("MongoSparkConnectorIntro")
    .config("spark.mongodb.input.uri", mongodbUri)
    .getOrCreate()

  val rdd: MongoRDD[Document] = MongoSpark.load(spark.sparkContext)

  val assembler = new VectorAssembler()
    .setInputCols(Array("plane_latitude", "plane_longitude", "airport_latitude", "airport_longitude"))
    .setOutputCol("features")

  import org.apache.spark.sql.functions._
  val analyticDf = rdd.toDF(analyticSchema)
//    .withColumnRenamed("localization.latitude", "plane_latitude")
//    .withColumnRenamed("localization.longitude", "plane_longitude")
//    .withColumnRenamed("arrivalAirport.latitude", "airport_latitude")
//    .withColumnRenamed("arrivalAirport.longitude", "airport_longitude")
    .selectExpr(
      "localization.latitude as plane_latitude",
      "localization.longitude as plane_longitude",
      "arrivalAirport.latitude as airport_latitude",
      "arrivalAirport.longitude as airport_longitude",
      "landedTimestamp")
  val featureDF = assembler.transform(analyticDf)

//  println(rdd.count())
//  print(rdd.first().toJson)
//
//  analyticDf.show()


  val lr = new LinearRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)
    .setFeaturesCol("features")
    .setLabelCol("landedTimestamp")

//  // Fit the model
//  val pipeline = new Pipeline().setStages(Array(assembler,lr))
//  val pipelineModel: PipelineModel = pipeline.fit(analyticDf)


  val lrModel: LinearRegressionModel = lr.fit(featureDF)

  // Print the coefficients and intercept for linear regression
  println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

  // Summarize the model over the training set and print out some metrics



  val trainingSummary = lrModel.summary
  println(s"numIterations: ${trainingSummary.totalIterations}")
  println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
  trainingSummary.residuals.show()
  println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
  println(s"r2: ${trainingSummary.r2}")


  val predictAssembler = new VectorAssembler()
    .setInputCols(Array("plane_latitude", "plane_longitude", "airport_latitude", "airport_longitude"))
  val dataDF = predictAssembler.transform(analyticDf)


//  lrModel.predict(dataDF)
  spark.close()

}
