package ziotest

import zio.test.*
import zio.test.Assertion.*
import zio.test.Gen.int

object Associativity extends ZIOSpecDefault:
  val spec = suite("ExampleSpec")(
    test("integer addition is associative") {
      check(int, int, int) { (x, y, z) =>
        val left = (x + y) + z
        val right = x + (y + z)
        assertTrue(left == right)
      }
    }
  )
