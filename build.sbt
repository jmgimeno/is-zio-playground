name := "is-zio-playground"

ThisBuild / scalaVersion := "3.7.0"

ThisBuild / libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.18",
  "dev.zio" %% "zio-test" % "2.1.18" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.18" % Test,
  "dev.zio" %% "zio-test-magnolia" % "2.1.18" % Test
)
