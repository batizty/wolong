import sbt.Keys._
import sbt._


name := "wolong"

organization := "com.weibo.datasys"

version := "0.1.0-SNAPSHOT"

/* scala versions and options */
scalaVersion := "2.11.8"

// These options will be used for *all* versions.
scalacOptions ++= Seq(
  "-deprecation"
  , "-unchecked"
  , "-encoding", "UTF-8"
  // "-optimise"   // this option will slow your build
)

scalacOptions ++= Seq(
  "-Yclosure-elim",
  "-Yinline"
)

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

val akkaVersion = "2.4.16"
val sprayVersion = "1.3.4"

/* dependencies */
libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "1.4.0"
  // -- Logging --
  , "ch.qos.logback" % "logback-classic" % "1.1.2"
  // -- Akka --
  , "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  , "com.typesafe.akka" %% "akka-actor" % akkaVersion
  , "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  , "com.typesafe.akka" %% "akka-cluster" % akkaVersion
  // -- Spray --
  , "io.spray" %% "spray-routing" % sprayVersion
  , "io.spray" %% "spray-can" % sprayVersion
  , "io.spray" %% "spray-httpx" % sprayVersion
  , "io.spray" %% "spray-client" % sprayVersion
  , "io.spray" %% "spray-testkit" % sprayVersion % "test"
  // -- Json --
  , "org.json4s" %% "json4s-native" % "3.2.11"
  , "com.typesafe.play" %% "play-json" % "2.4.0-M1"
  , "net.liftweb" % "lift-json_2.11" % "3.0.1"
  // -- Slick --
  , "com.typesafe.slick" %% "slick" % "3.2.0-M2"
  , "com.typesafe.slick" %% "slick-codegen" % "3.2.0-M2"
  // -- MySql --
  , "mysql" % "mysql-connector-java" % "5.1.39"
  // -- ScallopConf --
  , "org.rogach" % "scallop_2.11" % "2.0.5"

)

/** just for testing **/
libraryDependencies += "com.nokia" %% "mesos-scala-api" % "0.4.0"

resolvers += Resolver.url("sbt-plugin-releases-scalasbt", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))

