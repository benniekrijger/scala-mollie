import sbt._

object Dependencies {
  val common = Seq(
    "com.typesafe.akka" %% "akka-actor"       % "2.5.20",
    "com.typesafe.akka" %% "akka-testkit"     % "2.5.20" % "test",
    "com.typesafe.akka" %% "akka-http"        % "10.1.7",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.25.2",
    "org.json4s"        %% "json4s-jackson"   % "3.6.4",
    "org.json4s"        %% "json4s-ext"       % "3.6.4",
    "joda-time"         %  "joda-time"        % "2.10.1",
    "org.scalatest"     %% "scalatest"        % "3.0.5" % "test"
  )
}
