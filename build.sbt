ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "hangman_v1"
  )
libraryDependencies += "com.lihaoyi" %% "ujson" % "3.1.0"
