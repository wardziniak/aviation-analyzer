package com.wardziniak.aviation.analyzer.app

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}


object JdbcSampleApp extends App {



  case class Person(name: String, age: Int)

  import org.apache.spark.sql.functions._

  val session = SparkSession.builder.master("local[2]")
    .appName("NetworkWordCount").config("spark.driver.host", "localhost").getOrCreate()


  import session.implicits._
  val df: DataFrame = session.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "topicA")
    .option("startingOffsets", "earliest")
    .load()

  val schema = ScalaReflection.schemaFor[Person].dataType.asInstanceOf[StructType]


  session.read

  val people = df.selectExpr(s"CAST(value AS STRING) AS json")
    .select(from_json($"json", schema) as "data")
    .select("data.*").as[Person].map(p => p.copy(age = p.age+7))

  people.printSchema()

  val sQuery = people.writeStream.trigger(Trigger.ProcessingTime("5 second")).
    foreachBatch((peopleDataSet: Dataset[Person], n: Long) => {
    peopleDataSet.write.format("jdbc")
      .mode(SaveMode.Append)
      .option(JDBCOptions.JDBC_URL, "jdbc:postgresql://localhost:6543/catalogsviewer?user=pguser&password=pguser")
      .option(JDBCOptions.JDBC_TABLE_NAME, "people")
      .option(JDBCOptions.JDBC_DRIVER_CLASS, "org.postgresql.Driver")
      .save()
  }
  ).start()
//    peopleDataSet.write().format("jdbc")
//      .option(JDBCOptions.JDBC_URL, jdbcExporterParameters.url)
//      .option(JDBCOptions.JDBC_TABLE_NAME, jdbcExporterParameters.tableName)
//      .option(JDBCOptions.JDBC_DRIVER_CLASS, jdbcExporterParameters.jdbcDriver)

//  val sQuery: StreamingQuery = results.writeStream.trigger(Trigger.ProcessingTime("5 second"))
//    .option("checkpointLocation", "/tmp/checkpoint/")
//    .option("kafka.bootstrap.servers", "localhost:9092")
//    .option("topic", "topicAOut")
//    .format("kafka")
//    .start("/tmp/streamBar")



  sQuery.awaitTermination(60000)
}
