package fibers

import zio.*

object ForkJoinFailedFiberExample extends ZIOAppDefault {

  lazy val doSomething: ZIO[Any, String, Nothing] =
    ZIO.debug("do something!").delay(2.seconds) *> ZIO.fail("Boom")

  def run = for {
    _     <- ZIO.debug("Starting the program!")
    fiber <- doSomething.fork
    _     <- fiber.join // Fail with the error from the fiber
    _     <- ZIO.debug("The fiber has joined!")
  } yield ()
}
