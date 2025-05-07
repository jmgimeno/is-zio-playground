package ziotest

import zio.*
import zio.test.*
import zio.test.TestAspect.*

object CountDownSpec extends ZIOSpecDefault {

  val coundDown: ZIO[Any, Nothing, Unit] =
    ZIO
      .foreachDiscard(5 to 1 by -1) { i =>
        Console.printLine(i) *> ZIO.sleep(1.second)
      }
      .orDie

  val spec: Spec[Any, Nothing] = suite("count down")(
    test("prints 5 immediately and it's not finished") {
      for {
        fiber <- coundDown.fork
        _ <- TestClock.adjust(0.seconds)
        output <- TestConsole.output
        exit <- fiber.poll
      } yield assertTrue(output == Vector("5\n") && exit.isEmpty)
    },
    test("after 1 second it has printed 5 & 4 and it's not finished") {
      for {
        fiber <- coundDown.fork
        _ <- TestClock.adjust(1.second)
        output <- TestConsole.output
        exit <- fiber.poll
      } yield assertTrue(output == Vector("5\n", "4\n") && exit.isEmpty)
    },
    test("after 2 seconds prints a 3 and it's not finished") {
      for {
        fiber <- coundDown.fork
        _ <- TestClock.adjust(1.second)
        _ <- TestConsole.clearOutput
        _ <- TestClock.adjust(1.second)
        output <- TestConsole.output
        exit <- fiber.poll
      } yield assertTrue(output == Vector("3\n") && exit.isEmpty)
    },
    test("after 5 seconds prints numbers from 5 to 1 and it's finished") {
      for {
        fiber <- coundDown.fork
        _ <- TestClock.adjust(5.seconds)
        output <- TestConsole.output
        exit <- fiber.join
      } yield assertTrue(output == Vector("5\n", "4\n", "3\n", "2\n", "1\n"))
    }
  ) @@ silent
}
