package ziotest

import zio.*
import zio.test.*
import zio.test.Assertion.*

object TimeSpec extends ZIOSpecDefault:

  val goShopping: ZIO[Any, Nothing, Unit] =
    Console.printLine("Going shopping!").orDie.delay(1.hour)

  val spec = suite("ExampleSpec")(
    test("goShopping delays for one hour") {
      for
        fiber <- goShopping.fork
        _ <- TestClock.adjust(1.hour)
        _ <- fiber.join
      yield assertCompletes
    },
    test("goShopping delays for one hour (smart)") {
      for
        fiber <- goShopping.fork
        _ <- TestClock.adjust(1.hour)
        poll <- fiber.poll
      yield assertTrue(poll == Some(Exit.succeed(())))
    },
    test("goShopping is not finished before one hour") {
      for
        fiber <- goShopping.fork
        _ <- TestClock.adjust(59.minutes)
        poll <- fiber.poll
      yield assert(poll)(isNone)
    },
    test("goShopping is not finished before one hour (smart)") {
      for
        fiber <- goShopping.fork
        _ <- TestClock.adjust(59.minutes)
        poll <- fiber.poll
      yield assertTrue(poll == None)
    }
  )
