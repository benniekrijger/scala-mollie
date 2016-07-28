import sbt._

object  Dependencies {
  object Version {
    val akka = "2.4.8"
  }

  val common = Seq(
    "com.typesafe.akka" %% "akka-actor" % Version.akka,
    "com.typesafe.akka" %% "akka-testkit" % Version.akka % "test",
    "com.typesafe.akka" %% "akka-slf4j" % Version.akka,
    "com.typesafe.akka" %% "akka-http-core" % Version.akka,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.7.0",
    "org.json4s"        %% "json4s-native" % "3.4.0",
    "org.json4s"        %% "json4s-jackson" % "3.4.0",
    "joda-time"         %  "joda-time" % "2.9.4",
    "org.joda"          %  "joda-convert" % "1.8.1",
    "org.scalatest"     %% "scalatest" % "2.2.4" % "test",
    "ch.qos.logback"    %  "logback-classic" % "1.1.6"
  )
}