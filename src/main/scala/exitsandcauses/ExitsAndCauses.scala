package exitsandcauses

import zio.*
import java.text.NumberFormat
import java.io.IOException

object ExitsAndCauses extends ZIOAppDefault:

  val readInt: ZIO[Any, Nothing, Int] = Console.readLine.orDie.flatMap { str =>
    ZIO.succeed(str.toInt)
  }

  val readIntOrError: ZIO[Any, NumberFormatException, Int] =
    Console.readLine.orDie.flatMap { str =>
      ZIO.attempt(str.toInt).refineToOrDie[NumberFormatException]
    }

  val effect1: ZIO[Any, IOException, Unit] =
    Console.printLine("no cause")

  val effect2: ZIO[Any, Cause[IOException], Unit] =
    Console.printLine("with cause").sandbox

  // val effect3 = effect1.unsandbox

  val effect4: ZIO[Any, IOException, Unit] = effect2.unsandbox

  val run =
    for
      _ <- Console.print("Enter an integer: ")
      // exit <- readInt.exit
      // exit <- readIntOrError.exit
      exit <- readInt.sandbox.exit
      _ <- Console.printLine(s"exit=$exit")
    yield ()
