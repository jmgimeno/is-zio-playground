package introzio

import zio.*
import scala.io.StdIn

object Errors extends ZIOAppDefault:

  def writeLine(str: String): ZIO[Any, Nothing, Unit] =
    ZIO.attempt(println(str)).orDie

  def readInt(prompt: String): ZIO[Any, NumberFormatException, Int] =
    (ZIO.attempt(print(prompt)) *> ZIO.attempt(StdIn.readInt()))
      .refineToOrDie[NumberFormatException]

  val readAndSumTwoInts: ZIO[Any, NumberFormatException, Int] =
    for
      x <- readInt("Number1: ")
      y <- readInt("Number2: ")
    yield x + y

  def run: ZIO[Any, Nothing, Unit] =
    readAndSumTwoInts.foldZIO(
      e => writeLine(s"ERROR: Bad number ${e.getMessage}"),
      sum => writeLine(s"The sum is $sum")
    )
