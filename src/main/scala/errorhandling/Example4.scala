package errorhandling

import zio._
import java.io.IOException

object Example4 extends ZIOAppDefault {

  def run =
    for {
      a <- readNumber("Enter the first number  (a): ")
      b <- readNumber("Enter the second number (b): ").repeatUntil(_ != 0)
      r <- divide(a, b)
      _ <- Console.printLine(s"a / b: $r")
    } yield ()

  def parseInput(input: String): ZIO[Any, NumberFormatException, Int] = {
    val int: ZIO[Any, Throwable, Int] = ZIO.attempt(input.toInt)
    int.refineToOrDie[NumberFormatException]
  }

  def readNumber(msg: String): ZIO[Any, Nothing, Int] = {
    val read: ZIO[Any, IOException, String] =
      Console.print(msg) *> Console.readLine
    val ignoreIOE: ZIO[Any, Nothing, String] =
      read.orDie
    val readInt: ZIO[Any, NumberFormatException, Int] =
      ignoreIOE.flatMap(parseInput)
    readInt
      .retryWhile(_ => true)
      .orDie
  }

  def divide(a: Int, b: Int): ZIO[Any, Nothing, Int] = ZIO.succeed(a / b)
}
