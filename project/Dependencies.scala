import sbt.{ModuleID, _}

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
    lazy val analyzer: Seq[ModuleID] = Seq(
      logbackClassic, kafkaStreams, embeddedKafka, pureConfig
    ) ++ test
  }



  // Play
  lazy val playWsStandalone =  "com.typesafe.play" %% "play-ahc-ws-standalone" % Versions.playWsStandalone
  lazy val playWsStandaloneJson = "com.typesafe.play" %% "play-ws-standalone-json" % Versions.playWsStandalone
  lazy val playJson = "com.typesafe.play" %% "pKafkaDataPublisherlay-json" % "2.6.10"
  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.PureConfig

  // Serializer
  lazy val avro4sCore = "com.sksamuel.avro4s" %% "avro4s-core" % Versions.Avro4sCore



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

  // Logging
  lazy val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  lazy val logbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % Versions.logbackClassic
}
