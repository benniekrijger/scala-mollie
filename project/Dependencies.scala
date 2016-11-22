import sbt._

object  Dependencies {
  object Version {
    val akka = "2.4.14"
    val akkaHttp = "10.0.0-RC2"
  }

  val common = Seq(
    "com.typesafe.akka" %% "akka-actor" % Version.akka,
    "com.typesafe.akka" %% "akka-testkit" % Version.akka % "test",
    "com.typesafe.akka" %% "akka-slf4j" % Version.akka,
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.11.0-M4",
    "org.json4s"        %% "json4s-native" % "3.5.0",
    "org.json4s"        %% "json4s-jackson" % "3.5.0",
    "joda-time"         %  "joda-time" % "2.9.4",
    "org.joda"          %  "joda-convert" % "1.8.1",
    "org.scalatest"     %% "scalatest" % "3.0.0" % "test",
    "ch.qos.logback"    %  "logback-classic" % "1.1.6"
  )
}