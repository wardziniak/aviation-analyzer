import sbt.Keys._
import sbt.Setting
import sbt.librarymanagement.Resolver
import sbtassembly.AssemblyPlugin.autoImport.{ assembly, assemblyMergeStrategy }
import sbtassembly.{MergeStrategy, PathList}


object Common {


  /**
    * Settings shared among all projects
    */
  lazy val Settings = Seq(
    organization                 := "com.wardziniak.aviation",
    scalaVersion                 := Versions.scalaVersion,
    scalacOptions +=            "-Xexperimental",
    commonAssemblyMergeStrategy
  )

  /**
    * This one is shared among many projects. Together with EnvironmentConfigs.createAssemblySettingsInConfig
    * it makes the overridings magic.
    * Stay careful and keep it general!
    */
  lazy val commonAssemblyMergeStrategy: Setting[String => MergeStrategy] = assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", "eclipse.inf") => MergeStrategy.discard
    case PathList("logback.xml") => MergeStrategy.first
    case PathList("application.conf") => MergeStrategy.first
    case PathList("application.properties") => MergeStrategy.first
    case PathList("secret.conf") => MergeStrategy.first
    case PathList("secret.properties") => MergeStrategy.first
    case PathList("overview.html") => MergeStrategy.first
    case PathList(ps @ _*) if ps.last.endsWith(".class") => MergeStrategy.first
    case x => (assemblyMergeStrategy in assembly).value(x) //TODO MergeStrategy.defaultMergeStrategy
  }

  // "Confluent" at "http://packages.confluent.io/maven/"
//  val settings =
//    Map("aviation-api" -> (Common ++ Seq(
//
//    )),"aviation-common" -> Seq()
//  )
}
