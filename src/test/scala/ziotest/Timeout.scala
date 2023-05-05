package ziotest

import zio.*
import zio.test.*
import zio.test.Assertion.*

object Timeout extends ZIOSpecDefault:
  val spec = test("timeout") {
    for {
      fiber <- ZIO.sleep(5.minutes).timeout(1.minute).fork
      _ <- TestClock.adjust(1.minute)
      result <- fiber.join
    } yield assertTrue(result.isEmpty)
  }
