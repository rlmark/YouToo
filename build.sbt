name := "YouToo"

version := "0.1"

scalaVersion := "2.12.6"
scalacOptions += "-Ypartial-unification"

libraryDependencies := Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.18.3" % Test
)