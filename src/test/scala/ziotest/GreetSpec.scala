package ziotest

import zio.*
import zio.test.*

object GreetSpec extends ZIOSpecDefault:

  val greet: ZIO[Any, Nothing, Unit] =
    for
      name <- Console.readLine.orDie
      _ <- Console.printLine(s"Hello, $name").orDie
    yield ()

  val spec = test("salutes the entered name") {
    check(Gen.string) { name =>
      for
        _ <- TestConsole.feedLines(s"$name")
        _ <- TestConsole.silent(greet)
        output <- TestConsole.output
        _ <- TestConsole.clearOutput
      yield assertTrue(output == Vector(s"Hello, $name\n"))
    }
  }
