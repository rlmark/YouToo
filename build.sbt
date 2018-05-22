name := "YouToo"

version := "0.1"

scalaVersion := "2.12.6"
scalacOptions += "-Ypartial-unification"

val http4sVersion = "0.18.11"

libraryDependencies := Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "io.monix" %% "monix" % "3.0.0-RC1",
  "org.typelevel" %% "cats-core" % "1.0.1",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.18.3" % Test
)