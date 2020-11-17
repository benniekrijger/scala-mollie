import sbt._

object Dependencies {
  val common = Seq(
    "com.typesafe.akka" %% "akka-actor"       % "2.6.10",
    "com.typesafe.akka" %% "akka-stream"      % "2.6.10",
    "com.typesafe.akka" %% "akka-testkit"     % "2.6.10" % "test",
    "com.typesafe.akka" %% "akka-http"        % "10.2.1",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.35.2",
    "org.json4s"        %% "json4s-jackson"   % "3.6.10",
    "org.json4s"        %% "json4s-ext"       % "3.6.10",
    "joda-time"         %  "joda-time"        % "2.10.8",
    "org.scalatest"     %% "scalatest"        % "3.2.3" % "test"
  )
}
