package introzio

import zio.*

object Recursion extends ZIOAppDefault:

  val readInt: ZIO[Any, Throwable, Int] =
    for
      line <- Console.readLine
      n <- ZIO.attempt(line.toInt)
    yield n

  val readIntOrRetry: ZIO[Any, Nothing, Int] =
    readInt
      .orElse(
        Console
          .printLine("Please, enter a valid integer")
          .orDie
          *> readIntOrRetry
      )

  def run = readIntOrRetry
