val Organization = "com.github.kardapoltsev"
val SkipUpdate   = true
val CacheUpdate  = true

organization := Organization
name := "json4s-java-time"
scalaVersion := "2.13.4"
crossScalaVersions := Seq("2.10.6", "2.11.12", "2.12.7", scalaVersion.value)
organizationName := Organization
organizationHomepage := Some(url("https://github.com/kardapoltsev"))
parallelExecution in Test := true

initialize ~= { _ =>
  if (sys.props("java.specification.version") < "1.8")
    sys.error("Java 8 is required for this project.")
}
updateOptions := updateOptions.value.withCachedResolution(CacheUpdate)
//incOptions := incOptions.value.withNameHashing(true)
scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint"
)
scalacOptions ++= {
  if (scalaVersion.value.startsWith("2.10") || scalaVersion.value.startsWith("2.13")) {
    Seq.empty[String]
  } else {
    Seq(
      "-Ywarn-unused-import"
    )
  }
}

//sbt-release configuration
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseCrossBuild := true

//publish configuration
publishMavenStyle := true
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)
homepage := Some(url("https://github.com/kardapoltsev/astparser"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/kardapoltsev/astparser"),
    "scm:git@github.com:kardapoltsev/astparser.git"
  )
)
developers := List(
  Developer(
    id = "kardapoltsev",
    name = "Alexey Kardapoltsev",
    email = "alexey.kardapoltsev@gmail.com",
    url = url("https://github.com/kardapoltsev"))
)

licenses := Seq(("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")))
import de.heikoseeberger.sbtheader.AutomateHeaderPlugin
startYear := Some(2016)

val Json4sVersion = "3.6.10"
val json4sCore    = "org.json4s" %% "json4s-core" % Json4sVersion % "provided"
val json4sNative  = "org.json4s" %% "json4s-native" % Json4sVersion % "test"
val scalatest     = "org.scalatest" %% "scalatest" % "3.2.3" % "test"

libraryDependencies ++= Seq(
  json4sCore,
  json4sNative,
  scalatest
)

enablePlugins(AutomateHeaderPlugin)
enablePlugins(ScalafmtPlugin)
scalafmtOnCompile := true
