name := "cats-sandbox"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.1"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

// scalac options come from the sbt-tpolecat plugin so need to set any here

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)


scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-unchecked",
  "-feature",
  "-encoding",
  "utf8",
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-Xfatal-warnings",
  "-Ypartial-unification"
)