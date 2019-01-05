import sbt._

object Dependencies {
  object Version {
    val akka = "2.5.19"
    val akkaHttp = "10.1.7"
  }

  val common = Seq(
    "com.typesafe.akka" %% "akka-actor"       % Version.akka,
    "com.typesafe.akka" %% "akka-testkit"     % Version.akka % "test",
    "com.typesafe.akka" %% "akka-http"        % Version.akkaHttp,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.23.0",
    "org.json4s"        %% "json4s-jackson"   % "3.6.3",
    "org.json4s"        %% "json4s-ext"       % "3.6.3",
    "joda-time"         %  "joda-time"        % "2.10.1",
    "org.scalatest"     %% "scalatest"        % "3.0.5" % "test"
  )
}
