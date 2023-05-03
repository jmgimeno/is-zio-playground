package time

import zio.*
import zio.test.*
import zio.test.Assertion.*

object TimeSpec extends ZIOSpecDefault:

  val goShopping: ZIO[Any, Nothing, Unit] =
    Console.printLine("Going shopping!").orDie.delay(1.hour)

  def spec = suite("ExampleSpec")(
    test("goShopping delays for one hour") {
      for
        fiber <- goShopping.fork
        _ <- TestClock.adjust(1.hour)
        _ <- fiber.join
      yield assertCompletes
    },
    test("goShopping is not finished before one hour") {
      for
        fiber <- goShopping.fork
        _ <- TestClock.adjust(59.minutes)
        poll <- fiber.poll
      yield assert(poll)(isNone)
    }
  )
