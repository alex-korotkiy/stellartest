ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.3"

val circeVersion = "0.14.1"
lazy val akkaVersion = "2.6.9"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++=Seq(
  "com.lihaoyi" %% "requests" % "0.7.0",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)


lazy val root = (project in file("."))
  .settings(
    name := "root"
  )
