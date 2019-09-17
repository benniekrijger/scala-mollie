name := """scala-mollie"""

version := "0.24"

organization := "com.github.benniekrijger"

scalaVersion := "2.13.0"

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

cancelable in Global := true

libraryDependencies ++= Dependencies.common
