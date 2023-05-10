package exitsandcauses

import zio.*
import java.text.NumberFormat
import java.io.IOException
import zio.Cause.Empty
import zio.Cause.Fail
import zio.Cause.Die
import zio.Cause.Interrupt
import zio.Cause.Stackless
import zio.Cause.Then
import zio.Cause.Both

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

  val effect5 =
    effect1.foldCauseZIO(
      cause =>
        cause match
          case Fail(error, trace) => Console.printLine(s"Failed with $error")
          case Die(throwable, trace) =>
            Console.printLine(s"Died with $throwable")
          case _ => ZIO.die(new Exception("another case"))
      ,
      success => Console.printLine(s"Success with $success")
    )

    effect1.sandbox.foldZIO(
      cause =>
        cause match
          case Fail(error, trace) => Console.printLine(s"Failed with $error")
          case Die(throwable, trace) =>
            Console.printLine(s"Died with $throwable")
          case _ => ZIO.die(new Exception("another case"))
      ,
      success => Console.printLine(s"Success with $success")
    )

    effect1.foldZIO(
      ioEx => Console.printLine(s"Died with $ioEx"),
      success => Console.printLine(s"Success with $success")
    )

  val getANumber =
    for
      input <- Console.readLine("Enter a number: ").orDie
      result <- ZIO
        .attempt(360 / input.toInt)
        .refineToOrDie[NumberFormatException]
    yield result

    val getANumber2 =
      (for
        input <- Console.readLine("Enter a number: ")
        result <- ZIO
          .attempt(360 / input.toInt)
      yield result)
        .refineToOrDie[NumberFormatException]

  val run =
    for
      _ <- Console.print("Enter an integer: ")
      // exit <- readInt.exit
      // exit <- readIntOrError.exit
      exit <- readInt.sandbox.exit
      _ <- Console.printLine(s"exit=$exit")
    yield ()
