package refs

import zio.*

object Basics extends ZIOAppDefault:

  def printAndIncrement(ref: Ref[Int]): UIO[Unit] =
    for
      n <- ref.get
      _ <- Console.printLine(s"Current value is $n").orDie
      _ <- ref.set(n + 1)
    yield ()

  val program =
    for
      ref <- Ref.make(42)
      _ <- printAndIncrement(ref)
      _ <- printAndIncrement(ref)
      _ <- printAndIncrement(ref)
      _ <- printAndIncrement(ref)
    yield ()

  val run = program
