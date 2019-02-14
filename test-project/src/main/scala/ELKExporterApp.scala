import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark.sql._

object ELKExporterApp extends App {


  val conf = new SparkConf().setMaster("local[1]")
    .setAppName("NetworkWordCount")
    .set("spark.driver.bindAddress", "127.0.0.1")
    .set("es.nodes", "localhost")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)

  val t = Seq(1,3,4,5)
  val l: RDD[Int] = sc.parallelize(t)

  println(l.take(3))


//  val dd: RDD[(String, collection.Map[String, AnyRef])] = sc.esRDD("nuke-dev-stream-211-bridge-table")

  val df: DataFrame = sqlContext.read
    .format("org.elasticsearch.spark.sql").option("es.nodes", "localhost").load("newtest2")

//  val aa = dd.take(10)
//  println(aa)
}
