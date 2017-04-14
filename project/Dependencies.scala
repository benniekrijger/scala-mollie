import sbt._

object  Dependencies {
  object Version {
    val akka = "2.5.0"
    val akkaHttp = "10.0.5"
  }

  val common = Seq(
    "com.typesafe.akka" %% "akka-actor" % Version.akka,
    "com.typesafe.akka" %% "akka-testkit" % Version.akka % "test",
    "com.typesafe.akka" %% "akka-slf4j" % Version.akka,
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.15.0",
    "org.json4s"        %% "json4s-native" % "3.5.1",
    "org.json4s"        %% "json4s-jackson" % "3.5.1",
    "joda-time"         %  "joda-time" % "2.9.9",
    "org.joda"          %  "joda-convert" % "1.8.1",
    "org.scalatest"     %% "scalatest" % "3.0.1" % "test",
    "ch.qos.logback"    %  "logback-classic" % "1.2.3"
  )
}