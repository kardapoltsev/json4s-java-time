val Organization = "com.github.kardapoltsev"
val SkipUpdate   = true
val CacheUpdate  = true

organization := Organization
name := "json4s-java-time"
scalaVersion := "2.11.12"
crossScalaVersions := Seq("2.10.6", scalaVersion.value, "2.12.6")
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
  if (scalaVersion.value.startsWith("2.10")) {
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

val json4sCore   = "org.json4s"    %% "json4s-core"   % "3.5.4" % "provided"
val scalatest    = "org.scalatest" %% "scalatest"     % "3.0.5" % "test"
val json4sNative = "org.json4s"    %% "json4s-native" % "3.5.4" % "test"

scalacOptions in (Compile, doc) := Seq(
  "-encoding",
  "UTF-8",
  "-Xlint",
  "-deprecation",
  "-unchecked",
  "-Xfatal-warnings"
)
libraryDependencies ++= Seq(
  json4sCore,
  json4sNative,
  scalatest
)

enablePlugins(AutomateHeaderPlugin)
enablePlugins(ScalafmtPlugin)
scalafmtOnCompile := true
