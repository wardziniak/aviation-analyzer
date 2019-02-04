package com.wardziniak.aviation.analyzer.training

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.rdd.MongoRDD
import com.wardziniak.aviation.analyzer.app.StructureStreamApp.Person
import com.wardziniak.aviation.api.model.{AnalyticFlightSnapshot, TrainFlightSnapshot}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.bson.Document

object AviationTrainModelApp extends App {
  val mongodbUri = "mongodb://127.0.0.1/test.landed20190203_08_00"

  import org.apache.spark.sql.SparkSession

  val analyticSchema = ScalaReflection.schemaFor[AnalyticFlightSnapshot].dataType.asInstanceOf[StructType]

  val spark = SparkSession.builder()
    .master("local")
    .appName("MongoSparkConnectorIntro")
    .config("spark.mongodb.input.uri", mongodbUri)
    .getOrCreate()

  val rdd: MongoRDD[Document] = MongoSpark.load(spark.sparkContext)

//  val assembler = new VectorAssembler()
//    .setInputCols(Array("plane_latitude", "plane_longitude", "updated", "airport_latitude", "airport_longitude"))
//    .setOutputCol("features")

  val assembler = new VectorAssembler()
    .setInputCols(Array("distance"))
    .setOutputCol("features")

  import org.apache.spark.sql.functions._
  val analyticDf = rdd.toDF(analyticSchema)
    .selectExpr(
      "localization.latitude as plane_latitude",
      "localization.longitude as plane_longitude",
      "updated as updated",
      "arrivalAirport.latitude as airport_latitude",
      "arrivalAirport.longitude as airport_longitude",
      "flightNumber.iata as flightNumber_iata",
      "cast(distance as int) distance",
      "landedTimestamp")
    .withColumn("timeDifference", col("landedTimestamp") - col("updated"))
    .filter(col("timeDifference") > 0)
    .filter(col("distance") > 0).filter(col("distance") < 500000)
    .filter(col("landedTimestamp") > 0).limit(100)
    //.filter("flightNumber_iata == 'AF1235'")
  val featureDF: DataFrame = assembler.transform(analyticDf)

  val lr = new LinearRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)
    .setFeaturesCol("features")
    .setLabelCol("timeDifference")

  val lrModel: LinearRegressionModel = lr.fit(featureDF)
//
//  // Print the coefficients and intercept for linear regression
//  println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
  val results = lrModel.transform(featureDF)
    .select(col("distance"), col("timeDifference"), col("prediction").cast("long"))
  results.show(100)
//  results.printSchema()



//
//  analyticDf.printSchema()
//  println(analyticDf.select("plane_latitude", "plane_longitude", "airport_latitude", "airport_longitude").distinct().count())
//  println(analyticDf.count())

//  analyticDf.show(10)
  println(analyticDf.count())


  analyticDf.agg(min("distance"), max("distance"), mean("distance"),
    min("timeDifference"), max("timeDifference"), mean("timeDifference"),
    min("updated"), max("updated"), mean("updated").cast("long"),
    min("landedTimestamp"), max("landedTimestamp"), mean("landedTimestamp").cast("long")).show()

  spark.close()

  /**
    * +---------------+----------+
    * |landedTimestamp|prediction|
    * +---------------+----------+
    * |     1547991835|1548002221|
    * |     1547991868|1548002221|
    * |     1547991814|1548002095|
    * |     1547991920|1548002369|
    * |     1547991850|1548002317|
    * |     1547991843|1548002193|
    * |     1547992111|1548002179|
    * |     1547991830|1548002347|
    * |     1547992059|1548002362|
    * |     1547991942|1548002401|
    * |     1547992086|1548002347|
    * |     1547992059|1548002267|
    * |     1547991813|1548002193|
    * |     1547991842|1548001769|
    * |     1547991808|1548001770|
    * |     1547992104|1548002257|
    * |     1547991872|1548002255|
    * |     1547991910|1548002193|
    * |     1547992104|1548002131|
    * |     1547991853|1548001796|
    * +---------------+----------+
    */



  // Summarize the model over the training set and print out some metrics

  //  val trainingSummary = lrModel.summary
  //  println(s"numIterations: ${trainingSummary.totalIterations}")
  //  println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
  //  trainingSummary.residuals.show()
  //  println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
  //  println(s"r2: ${trainingSummary.r2}")

}
