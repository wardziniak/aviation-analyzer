import sbt.{ModuleID, _}

object Dependencies {

  object Aviation {
    lazy val api: Seq[ModuleID] = Seq() ++ test
    lazy val common: Seq[ModuleID] = Seq() ++ test
    lazy val importer: Seq[ModuleID] = Seq(playJson, playWsStandalone) ++ test
  }

  lazy val playWsStandalone =  "com.typesafe.play" %% "play-ahc-ws-standalone" % Versions.playWsStandalone
  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.6.10"

  // Tests
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test

  lazy val test: Seq[ModuleID] = Seq(scalaTest)

  // Kafka
  val kafkaClient: ModuleID = "org.apache.kafka" % "kafka-clients" % Versions.kafka
  val kafkaStreams: ModuleID = "org.apache.kafka" % "kafka-streams" % Versions.kafka
}
