name := "is-zio-playground"

scalaVersion := "3.8.3"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.25",
  "dev.zio" %% "zio-test" % "2.1.25" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.25" % Test,
  "dev.zio" %% "zio-test-magnolia" % "2.1.25" % Test
)
