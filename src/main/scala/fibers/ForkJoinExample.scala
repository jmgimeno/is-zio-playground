package fibers

import zio.*

object ForkJoinExample extends ZIOAppDefault {

  lazy val doSomething: UIO[Unit] =
    ZIO.debug("do something!").delay(10.seconds)

  lazy val doSomethingElse: UIO[Unit] =
    ZIO.debug("do something else!").delay(2.seconds)

  val run = for {
    _     <- ZIO.debug("Starting the program!")
    fiber <- doSomething.fork
    _     <- doSomethingElse
    _     <- fiber.join // Wait for the fiber to complete
    _     <- ZIO.debug("The fiber has joined!")
  } yield ()
}
