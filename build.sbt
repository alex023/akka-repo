name := "akka-repo"

val basicSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.3",

  //  crossScalaVersions := Seq("2.11.11", "2.12.3"), // 用于跨多版本Scala编译，一般只在framework或library时需要。

  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.5.4"
)

lazy val root = Project(id = "akka-repo-root", base = file("."))
  .aggregate(client, server, base)

lazy val base = project.in(file("base"))
  .settings(basicSettings: _*)

lazy val server = project.in(file("server"))
  .dependsOn(base)
  .settings(basicSettings: _*)

lazy val client = project.in(file("client"))
  .dependsOn(base)
  .settings(basicSettings: _*)

