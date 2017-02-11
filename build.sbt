name := "PetriNet"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint:_", "-Xlog-implicits")
scalacOptions in (Compile, doc) ++= Seq("-unchecked", "-deprecation", "-diagrams", "-implicits", "-skip-packages", "samples")

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2",
  "org.apache.spark" % "spark-core_2.11" % "2.0.2"
)

// Consider turning it off for Option2Iterable
wartremoverWarnings in (Compile, compile) ++= Warts.allBut(Wart.Option2Iterable, Wart.NonUnitStatements, Wart.NoNeedForMonad)
coverageEnabled in Test:= true

logBuffered in Test := false

// TODO make example files ignoring work
mappings in (Compile, packageBin) ~= { _.filter(!_._1.getName.endsWith(".java")) }
// mainClass in (Compile, packageBin) := Some("petrify.spark.BFS")
publishArtifact in (Compile, packageBin) := true
