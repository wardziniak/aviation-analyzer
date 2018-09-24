import sbt.Keys.{organization, scalaVersion}

object Common {


  /**
    * Settings shared among all projects
    */
  lazy val Settings = Seq(
    organization                 := "com.wardziniak.aviation",
    scalaVersion                 := Versions.scalaVersion
  )

//  val settings =
//    Map("aviation-api" -> (Common ++ Seq(
//
//    )),"aviation-common" -> Seq()
//  )
}
