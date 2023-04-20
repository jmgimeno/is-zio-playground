package hello

import zio.*
import zio.test.*
import zio.test.Assertion.*

object HelloZIOSuite extends ZIOSpecDefault:
  val spec = suite("HelloZIOSpec")(
    test("message") {
      assertTrue(HelloZIO.msg == "Hello, ZIO")
    },
    test("printing") {
      for
        _ <- HelloZIO.run
        output <- TestConsole.output
      yield assertTrue(output(0) == "Hello, ZIO\n")
    }
  )
