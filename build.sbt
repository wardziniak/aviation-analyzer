import Dependencies._


lazy val `aviation-api` = project.
  settings(description := "Aviation analyzer APIs").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Api).
  settings(libraryDependencies ++= Dependencies.Aviation.api)

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
  settings(libraryDependencies ++= Dependencies.Aviation.importer)

lazy val `pre-processing` = project.
  dependsOn(`aviation-api`).
  dependsOn(`aviation-common`).
  settings(description := "Aviation data Analyzer").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Analyzer).
  settings(libraryDependencies ++= Dependencies.Aviation.preProcessing)

lazy val `data-analyzer` = project.
  dependsOn(`aviation-api`).
  dependsOn(`aviation-common`).
  settings(description := "Aviation data Analyzer").
  settings(Common.Settings: _*).
  settings(version := Versions.Aviation.Analyzer).
  settings(libraryDependencies ++= Dependencies.Aviation.analyzer)
