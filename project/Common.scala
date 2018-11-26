import sbt.Keys.{organization, scalaVersion, scalacOptions}

object Common {


  /**
    * Settings shared among all projects
    */
  lazy val Settings = Seq(
    organization                 := "com.wardziniak.aviation",
    scalaVersion                 := Versions.scalaVersion,
    scalacOptions +=            "-Xexperimental"
  )

//  val settings =
//    Map("aviation-api" -> (Common ++ Seq(
//
//    )),"aviation-common" -> Seq()
//  )
}
