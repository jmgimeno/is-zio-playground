package environment

import zio.*
import java.io.IOException

object Basics2 extends ZIOAppDefault:

  val needsAnIntAndString: ZIO[Int & String, IOException, Unit] =
    for
      int <- ZIO.service[Int]
      str <- ZIO.service[String]
      _ <- Console.printLine(s"$str -> $int")
    yield ()

  val environmentWithIntAndString: ZEnvironment[Int & String] =
    ZEnvironment(42, "potato")

  val doesNotNeedAnything: ZIO[Any, IOException, Unit] =
    needsAnIntAndString.provideEnvironment(environmentWithIntAndString)

  val run = doesNotNeedAnything
