package environment

import zio.*
import java.io.IOException

object Basics extends ZIOAppDefault:

  val needsAnInt: ZIO[Int, IOException, Unit] =
    for
      int <- ZIO.service[Int]
      _ <- Console.printLine(s"Integer -> $int")
    yield ()

  val environmentWithInt: ZEnvironment[Int] = ZEnvironment(42)

  val doesNotNeedAnything: ZIO[Any, IOException, Unit] =
    needsAnInt.provideEnvironment(environmentWithInt)

  val run = doesNotNeedAnything
