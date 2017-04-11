name := """scala-mollie"""

version := "0.13"

organization := "com.github.benniekrijger"

crossScalaVersions := Seq("2.11.8", "2.12.1")
scalaVersion := crossScalaVersions.value.head
crossVersion := CrossVersion.binary

libraryDependencies ++= Dependencies.common
