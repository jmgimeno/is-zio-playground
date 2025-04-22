name := "is-zio-playground"

ThisBuild / scalaVersion := "3.6.4"

ThisBuild / libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.17",
  "dev.zio" %% "zio-test" % "2.1.17" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.17" % Test,
  "dev.zio" %% "zio-test-magnolia" % "2.1.17" % Test
)
