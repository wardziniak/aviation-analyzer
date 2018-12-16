import Dependencies._


lazy val `aviation-api` = project.
  settings(description := "Aviation analyzer APIs").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Api).
  settings(libraryDependencies ++= Dependencies.Aviation.api)
  //settings(crossScalaVersions := Seq("2.12.2"))

lazy val `aviation-common` = project.
  dependsOn(`aviation-api`).
  settings(description := "Aviation analyzer commons").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Common).
  settings(libraryDependencies ++= Dependencies.Aviation.common)

lazy val `aviation-importer` = project.
  dependsOn(`aviation-api`).
  dependsOn(`aviation-common`).
  settings(description := "Aviation data importer").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Importer).
  settings(libraryDependencies ++= Dependencies.Aviation.importer).
  settings(scalaVersion := Versions.scalaVersion)

lazy val `pre-processing` = project.
  dependsOn(`aviation-api`).
  dependsOn(`aviation-common`).
  settings(description := "Aviation data Analyzer").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Analyzer).
  settings(libraryDependencies ++= Dependencies.Aviation.preProcessing).
  settings(scalaVersion := Versions.scalaVersion)

lazy val `data-analyzer` = project.
  dependsOn(`aviation-api`).
  dependsOn(`aviation-common`).
  settings(description := "Aviation data Analyzer").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Analyzer).
  settings(libraryDependencies ++= Dependencies.Aviation.analyzer).
  settings(scalaVersion := Versions.scalaVersion)
