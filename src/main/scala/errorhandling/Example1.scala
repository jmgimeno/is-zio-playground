package errorhandling

import zio._
import java.io.IOException

object Example1 extends ZIOAppDefault {

  def run =
    for {
      a <- readNumber("Enter the first number  (a): ")
      b <- readNumber("Enter the second number (b): ")
      r <- divide(a, b)
      _ <- Console.printLine(s"a / b: $r")
    } yield ()

  def readNumber(msg: String): ZIO[Any, IOException, Int] =
    Console.print(msg) *> Console.readLine.map(_.toInt)

  def divide(a: Int, b: Int): ZIO[Any, Nothing, Int] =
    if (b == 0)
      ZIO.die(new ArithmeticException("divide by zero")) // unexpected error
    else
      ZIO.succeed(a / b)
}
