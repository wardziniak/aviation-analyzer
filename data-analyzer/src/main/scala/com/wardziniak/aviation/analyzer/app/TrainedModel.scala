package com.wardziniak.aviation.analyzer.app



import java.util

import com.wardziniak.aviation.api.model.AnalyticFlightSnapshot
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

object TrainedModel extends App {

  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()

  val schema = ScalaReflection.schemaFor[AnalyticFlightSnapshot].dataType.asInstanceOf[StructType]

  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext

//  val conf: SparkConf = new SparkConf()
//  val sc: SparkContext = new SparkContext(conf);
//  val hiveContext: HiveContext = new HiveContext(sc);
//  hiveContext.setConf("hive.metastore.uris", "thrift://METASTORE:9083");


}
