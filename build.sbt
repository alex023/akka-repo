import sbt.Keys.libraryDependencies

PB.targets in Compile := Seq(
  scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
)


name := "akka-repo"
val basicSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.4",

  //  crossScalaVersions := Seq("2.11.11", "2.12.3"), // 用于跨多版本Scala编译，一般只在framework或library时需要。
  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.8",
  libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.5.8",
  libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % Test,
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP9" % Test,
  libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.6.7",
  libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.1",
  libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4",
  libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.8-dmr"

)

lazy val root = Project(id = "akka-repo-root", base = file("."))
  .aggregate(auth, connect, union, world, base)


lazy val auth = project.in(file("auth"))
  .dependsOn(base)
  .settings(basicSettings: _*)

lazy val connect = project.in(file("connect"))
  .dependsOn(base, union)
  .settings(basicSettings: _*)

lazy val base = project.in(file("base"))
  .settings(basicSettings: _*)

lazy val union = project.in(file("union"))
  .dependsOn(base)
  .settings(basicSettings: _*)

lazy val world = project.in(file("world"))
  .dependsOn(base)
  .settings(basicSettings: _*)