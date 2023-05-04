package ziotest

import zio.*
import zio.test.*
import zio.test.Assertion.*

object BasicTest extends ZIOSpecDefault:

  def spec = test("simple zio succeed") {
    assertZIO(ZIO.succeed(1 + 1))(equalTo(2))
  }
