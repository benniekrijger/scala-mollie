name := """scala-mollie"""

version := "0.27"

organization := "com.github.benniekrijger"

scalaVersion := "2.13.3"

publishConfiguration := publishConfiguration.value.withOverwrite(true)

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

cancelable in Global := true

libraryDependencies ++= Dependencies.common
