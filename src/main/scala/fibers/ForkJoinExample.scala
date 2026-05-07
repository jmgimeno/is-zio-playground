package fibers

import zio.*

object ForkJoinExample extends ZIOAppDefault {

  lazy val doSomething: ZIO[Any, Nothing, Int] =
    ZIO.debug("do something!").delay(10.seconds) *> ZIO.succeed(42)

  lazy val doSomethingElse: ZIO[Any, Nothing, Unit] =
    ZIO.debug("do something else!").delay(2.seconds)

  val run = for {
    _     <- ZIO.debug("Starting the program!")
    fiber <- doSomething.fork
    _     <- doSomethingElse
    res   <- fiber.join // Wait for the fiber to complete
    _     <- ZIO.debug(s"The fiber has joined with vule $res!")
  } yield ()
}
