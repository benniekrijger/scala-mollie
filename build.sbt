name := """scala-mollie"""

version := "0.11"

organization := "com.github.benniekrijger"

crossScalaVersions := Seq("2.11.8", "2.12.0")
scalaVersion := crossScalaVersions.value.head
crossVersion := CrossVersion.binary

libraryDependencies ++= Dependencies.common
