import Dependencies._


lazy val `aviation-api` = project.
  settings(description := "Aviation analyzer APIs").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Api).
  settings(libraryDependencies ++= Dependencies.Aviation.api)

lazy val `aviation-common` = project.
  settings(description := "Aviation analyzer commons").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Common).
  settings(libraryDependencies ++= Dependencies.Aviation.common)

lazy val `aviation-importer` = project.
  dependsOn(`aviation-api`).
  settings(description := "Aviation data importer").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Importer).
  settings(libraryDependencies ++= Dependencies.Aviation.importer)



//lazy val root = (project in file(".")).
//  settings(
//    inThisBuild(List(
//      organization := "com.example",
//      scalaVersion := "2.12.6",
//      version      := "0.1.0-SNAPSHOT"
//    )),
//    name := "aviation-analyzer",
//    libraryDependencies += scalaTest % Test
//  )
