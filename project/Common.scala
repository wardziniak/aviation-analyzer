import sbt.Keys._
import sbt.librarymanagement.Resolver


object Common {


  /**
    * Settings shared among all projects
    */
  lazy val Settings = Seq(
    organization                 := "com.wardziniak.aviation",
    scalaVersion                 := Versions.scalaVersion,
    scalacOptions +=            "-Xexperimental"
  )

  // "Confluent" at "http://packages.confluent.io/maven/"
//  val settings =
//    Map("aviation-api" -> (Common ++ Seq(
//
//    )),"aviation-common" -> Seq()
//  )
}
