package errorhandling

import zio._
import java.io.IOException

object Example3 extends ZIOAppDefault {

  def run =
    for {
      a <- readNumber("Enter the first number  (a): ")
      b <- readNumber("Enter the second number (b): ").repeatUntil(_ != 0)
      r <- divide(a, b)
      _ <- Console.printLine(s"a / b: $r")
    } yield ()

  def parseInput(input: String): ZIO[Any, NumberFormatException, Int] =
    ZIO.attempt(input.toInt).refineToOrDie[NumberFormatException]

  def readNumber(msg: String): ZIO[Any, IOException, Int] =
    (Console.print(msg) *> Console.readLine.flatMap(parseInput))
      .retryUntil(!_.isInstanceOf[NumberFormatException])
      .refineToOrDie[IOException]

  def divide(a: Int, b: Int): ZIO[Any, Nothing, Int] = ZIO.succeed(a / b)
}
