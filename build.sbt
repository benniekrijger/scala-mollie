name := """scala-mollie"""

version := "0.25"

organization := "com.github.benniekrijger"

scalaVersion := "2.13.1"

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

cancelable in Global := true

libraryDependencies ++= Dependencies.common
