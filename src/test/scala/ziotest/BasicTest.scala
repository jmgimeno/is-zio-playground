package ziotest

import zio.*
import zio.test.*
import zio.test.Assertion.*

object BasicTest extends ZIOSpecDefault:

  val spec = suite("simple zio succeed")(
    test("classic assertion") {
      assertZIO(ZIO.succeed(1 + 1))(equalTo(2))
    },
    test("smart assertion") {
      for result <- ZIO.succeed(1 + 1)
      yield assertTrue(result == 2)
    }
  )
