package ziotest

import zio.test.*
import zio.test.Assertion.*

import safe.SafeDivision.*

object SafeDivisionSpec extends ZIOSpecDefault:

  val spec = suite("safe division")(
    test("divides ok if denominator not zero") {
      assertZIO(safeDivision(9, 3))(equalTo(3))
    },
    test("divides ok if denominator not zero (alt)") {
      assertZIO(safeDivision(9, 3).either)(isRight(equalTo(3)))
    },
    test("divides ok if denominator not zero (smart)") {
      for either <- safeDivision(9, 3).either
      yield assertTrue(either == Right(3))
    },
    test("division by zero fails with unit") {
      assertZIO(safeDivision(9, 0).exit)(fails(isUnit))
    },
    test("division by zero fails with unit (alt)") {
      assertZIO(safeDivision(9, 0).either)(isLeft(isUnit))
    },
    test("division by zero fails with unit (smart)") {
      for either <- safeDivision(9, 0).either
      yield assertTrue(either == Left(()))
    }
  )
