package fibers

import zio.*

object InterruptFiberExample extends ZIOAppDefault {
  lazy val doSomething: ZIO[Any, Nothing, Unit] =
    ZIO
      .debug("some long running task!")
      .repeat(Schedule.spaced(2.seconds))
      .unit
      .onInterrupt(
        ZIO.debug("Begin cleanup") *>
          ZIO.sleep(5.seconds) *>
          ZIO.debug("End cleanup"))

  val run =
    for {
      _ <- ZIO.debug("Starting the program!")
      fiber <- doSomething.fork
      _ <- ZIO.sleep(5.seconds)
      _ <- fiber.interrupt
      _ <- ZIO.debug("The fiber has been interrupted!")
    } yield ()
}
