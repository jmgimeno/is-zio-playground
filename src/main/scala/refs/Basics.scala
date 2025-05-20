package refs

import zio.*

object Basics extends ZIOAppDefault:

  def printAndIncrement(ref: Ref[Int]): ZIO[Any, Nothing, Unit] =
    for
      n <- ref.get
      _ <- Console.printLine(s"Current value is $n").orDie
      _ <- ref.set(n + 1)
    yield ()

  val program: ZIO[Any, Nothing, Unit] =
    for
      ref <- Ref.make(42)
      _ <- printAndIncrement(ref)
      _ <- printAndIncrement(ref)
      _ <- printAndIncrement(ref)
      _ <- printAndIncrement(ref)
    yield ()

  val program2 =
    val makeRef: ZIO[Any, Nothing, Ref[Int]] = Ref.make(0)

    val makeRef1: ZIO[Any, Nothing, Ref[Int]] = makeRef
    val makeRef2: ZIO[Any, Nothing, Ref[Int]] = makeRef

    for
      ref1 <- makeRef
      ref2 <- makeRef
      _ <- ref1.update(_ + 1)
      _ <- ref2.update(_ + 1)
      l <- ref1.get
      r <- ref2.get
    yield (l, r)

  val run = program2.debug
