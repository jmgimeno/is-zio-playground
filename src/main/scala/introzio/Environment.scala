package introzio

import zio.*

import scala.io.StdIn

// This mechanism is uses for DEPENDENCY INJECTION

object Environment extends ZIOAppDefault:

  trait MyLogger:
    def log(s: String): ZIO[Any, Nothing, Unit]

  // The real ZIO.environment returns a ZEnvironment
  // we must use get to access what is inside

  def writeLine(str: String): ZIO[Any, Nothing, Unit] =
    ZIO.attempt(println(str)).orDie

  def readInt(prompt: String): ZIO[Any, NumberFormatException, Int] =
    (ZIO.attempt(print(prompt)) *> ZIO.attempt(StdIn.readInt()))
      .refineToOrDie[NumberFormatException]

  val readAndSumTwoIntsWithLogging: ZIO[MyLogger, NumberFormatException, Int] =
    for
      x <- readInt("Number1: ")
      logger <- ZIO.environment[MyLogger]
      _ <- logger.get.log(s"I've read $x")
      y <- readInt("Number2: ")
      _ <- logger.get.log(s"I've also read $y")
    yield x + y

  object ConsoleLogger extends MyLogger:
    def log(s: String): ZIO[Any, Nothing, Unit] = writeLine(s)

  object NullLogger extends MyLogger:
    def log(s: String): ZIO[Any, Nothing, Unit] = ZIO.unit

  val run: ZIO[Any, Nothing, Unit] =
    readAndSumTwoIntsWithLogging
      .foldZIO(
        e => writeLine(s"ERROR found $e"),
        result => writeLine(s"The sum is $result")
      )
      .provideEnvironment(ZEnvironment(ConsoleLogger))
