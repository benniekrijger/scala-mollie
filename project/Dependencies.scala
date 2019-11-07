import sbt._

object Dependencies {
  val common = Seq(
    "com.typesafe.akka" %% "akka-actor"       % "2.6.0",
    "com.typesafe.akka" %% "akka-testkit"     % "2.6.0" % "test",
    "com.typesafe.akka" %% "akka-http"        % "10.1.10",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.29.1",
    "org.json4s"        %% "json4s-jackson"   % "3.6.7",
    "org.json4s"        %% "json4s-ext"       % "3.6.7",
    "joda-time"         %  "joda-time"        % "2.10.5",
    "org.scalatest"     %% "scalatest"        % "3.0.8" % "test"
  )
}
