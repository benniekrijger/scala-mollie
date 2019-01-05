name := """scala-mollie"""

version := "0.21"

organization := "com.github.benniekrijger"

scalaVersion := "2.12.8"

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

cancelable in Global := true

libraryDependencies ++= Dependencies.common
