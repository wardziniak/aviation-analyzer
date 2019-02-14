import sbt.{ExclusionRule, ModuleID, _}

object Dependencies {

  object Aviation {
    lazy val api: Seq[ModuleID] = Seq() ++ test
    lazy val common: Seq[ModuleID] = Seq(avro4sCore, kafkaClient, scalaLogging, embeddedKafka, specs2Core) ++ test
    lazy val importer: Seq[ModuleID] = Seq(
      logbackClassic,
      playWsStandaloneJson,
      playWsStandalone,
      specs2Core,
      specs2Mock,
      playFakeWsStandalone,
      embeddedKafka, pureConfig) ++ test
    lazy val preProcessing: Seq[ModuleID] = Seq(
      logbackClassic, kafkaStreamsScala, embeddedKafka, pureConfig, specs2Core, kafkaStreamsTest, json4sJackson, zkClient
    ).map(_.exclude("org.slf4j", "*")) //++ test
    lazy val analyzer: Seq[ModuleID] = Seq(
      postgresqlJdbcDriver, sparkCore,
      logbackClassic, sparkStreaming, sparkSql.excludeAll(ExclusionRule(organization = "org.json4s")),
      sparkSqlKafka.excludeAll(ExclusionRule(organization = "org.json4s")),
      sparkStreamingKafka.excludeAll(ExclusionRule(organization = "org.json4s")),
      sparkMlLib.excludeAll(ExclusionRule(organization = "org.json4s")),
      mongoSparkConnector,
      elasticsearchHadoop,
      //sparkTest.excludeAll(ExclusionRule(organization = "org.json4s")),
        embeddedKafka.excludeAll(ExclusionRule(organization = "org.json4s"))
    )
    lazy val aviationKafkaConnect: Seq[ModuleID] = Seq(
      kafkaConnect, kafkaConnectTransforms, kafkaConnectJson, conluentJdbcSink,kafkaConnectRuntime, springKafka
    )
    lazy val testProject: Seq[ModuleID] = Seq(sparkCore, elasticsearchHadoop, sparkSql)
    lazy val dataProcessor: Seq[ModuleID] = Seq(logbackClassic, kafkaStreamsScala, kafkaStreamsTest, pureConfig, specs2Core)
  }



  // Play
  lazy val playWsStandalone =  "com.typesafe.play" %% "play-ahc-ws-standalone" % Versions.playWsStandalone
  lazy val playWsStandaloneJson = "com.typesafe.play" %% "play-ws-standalone-json" % Versions.playWsStandalone
  lazy val playJson = "com.typesafe.play" %% "pKafkaDataPublisherlay-json" % "2.6.10"
  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.PureConfig

  // Serializer
  lazy val avro4sCore = "com.sksamuel.avro4s" %% "avro4s-core" % Versions.Avro4sCore
  lazy val json4sJackson = "org.json4s" %% "json4s-jackson" % Versions.Json4sJackson

  // Tests
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test
  lazy val specs2Mock = "org.specs2" %% "specs2-mock" % Versions.specs2Mock % Test
  lazy val specs2Core =  "org.specs2" %% "specs2-core" % Versions.specs2Mock % Test
  lazy val playMockWS =  "de.leanovate.play-mockws" %% "play-mockws" % Versions.playMockWS % Test
  lazy val playFakeWsStandalone = "org.f100ded.play" %% "play-fake-ws-standalone" % "1.1.0"
  lazy val embeddedKafka = "net.manub" %% "scalatest-embedded-kafka" % Versions.embeddedKafka % Test

  lazy val test: Seq[ModuleID] = Seq(scalaTest)

  // Kafka
  val kafkaClient: ModuleID = "org.apache.kafka" % "kafka-clients" % Versions.kafka
  val kafkaStreams: ModuleID = "org.apache.kafka" % "kafka-streams" % Versions.kafka
  val kafkaStreamsScala: ModuleID = "org.apache.kafka" %% "kafka-streams-scala" % Versions.kafka
  val kafkaStreamsTest: ModuleID = "org.apache.kafka" % "kafka-streams-test-utils" % Versions.kafka % Test

  val kafkaConnect: ModuleID = "org.apache.kafka" % "connect-api" % Versions.kafka
  val kafkaConnectTransforms: ModuleID = "org.apache.kafka" % "connect-transforms" % Versions.kafka
  val kafkaConnectJson: ModuleID = "org.apache.kafka" % "connect-json" % Versions.kafka
  val kafkaConnectRuntime: ModuleID =  "org.apache.kafka" % "connect-runtime" % Versions.kafka
  val conluentJdbcSink: ModuleID = "io.confluent" % "kafka-connect-jdbc" % Versions.Confluent


  // Spark
  val sparkStreaming: ModuleID = "org.apache.spark" %% "spark-streaming" % Versions.Spark
  val sparkSqlKafka = "org.apache.spark" %% "spark-sql-kafka-0-10" % Versions.Spark
  val sparkStreamingKafka: ModuleID =  "org.apache.spark" %% "spark-streaming-kafka-0-10" % Versions.Spark
  val sparkCore = "org.apache.spark" %% "spark-core" %  Versions.Spark
  val sparkSql: ModuleID = "org.apache.spark" %% "spark-sql" % Versions.Spark
  val sparkMlLib: ModuleID = "org.apache.spark" %% "spark-mllib" % Versions.Spark
  val sparkTest: ModuleID = "com.holdenkarau" %% "spark-testing-base" % "2.3.1_0.10.0" % "test"
  val mongoSparkConnector: ModuleID = "org.mongodb.spark" %% "mongo-spark-connector" % Versions.MongoSparkConnector
  val elasticsearchHadoop: ModuleID = "org.elasticsearch" % "elasticsearch-hadoop" % "6.1.0"

  val springKafka = "org.springframework.kafka" % "spring-kafka" % "2.2.2.RELEASE"

  val zkClient: ModuleID = "com.101tec" % "zkclient" % "0.10"

  val jackson: ModuleID = "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.7.4"

  // Logging
  lazy val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  lazy val logbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % Versions.logbackClassic

  lazy val dispatch: ModuleID = "net.databinder" %% "dispatch" % "0.8.0"

  lazy val postgresqlJdbcDriver = "org.postgresql" % "postgresql" % "42.1.1"
}
