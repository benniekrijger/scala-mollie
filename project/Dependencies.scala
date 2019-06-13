import sbt._

object Dependencies {
  val common = Seq(
    "com.typesafe.akka" %% "akka-actor"       % "2.5.23",
    "com.typesafe.akka" %% "akka-testkit"     % "2.5.23" % "test",
    "com.typesafe.akka" %% "akka-http"        % "10.1.8",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.26.0",
    "org.json4s"        %% "json4s-jackson"   % "3.6.6",
    "org.json4s"        %% "json4s-ext"       % "3.6.6",
    "joda-time"         %  "joda-time"        % "2.10.2",
    "org.scalatest"     %% "scalatest"        % "3.0.8" % "test"
  )
}
