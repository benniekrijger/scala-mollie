import sbt._

object Dependencies {
  val common = Seq(
    "com.typesafe.akka" %% "akka-actor"       % "2.5.25",
    "com.typesafe.akka" %% "akka-testkit"     % "2.5.25" % "test",
    "com.typesafe.akka" %% "akka-http"        % "10.1.9",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.28.0",
    "org.json4s"        %% "json4s-jackson"   % "3.6.7",
    "org.json4s"        %% "json4s-ext"       % "3.6.7",
    "joda-time"         %  "joda-time"        % "2.10.3",
    "org.scalatest"     %% "scalatest"        % "3.0.8" % "test"
  )
}
