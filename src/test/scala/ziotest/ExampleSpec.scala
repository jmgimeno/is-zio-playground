package ziotest

import zio.*
import zio.test.*
import zio.test.Assertion.*

object ExampleSpec extends ZIOSpecDefault:

  val spec = suite("ExampleSpec")(
    suite("fails")(
      test("classic") {
        for exit <- ZIO.attempt(1 / 0).catchAll(_ => ZIO.fail(())).exit
        yield assert(exit)(fails(isUnit))
      },
      test("smart") {
        for exit <- ZIO.attempt(1 / 0).catchAll(_ => ZIO.fail(())).exit
        yield exit match
          case Exit.Failure(Cause.Fail((), _)) => assertTrue(true)
          case _                               => assertTrue(false)
      },
      test("smart (simpler)") {
        for exit <- ZIO.attempt(1 / 0).catchAll(_ => ZIO.fail(())).exit
        yield assertTrue(exit == Exit.fail(()))
      }
    )
  )
