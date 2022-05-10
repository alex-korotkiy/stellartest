ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++=Seq(
  "com.lihaoyi" %% "requests" % "0.7.0"
)


lazy val root = (project in file("."))
  .settings(
    name := "RawConcept"
  )
