package fibers

import zio.*

object InterruptFiberExample extends ZIOAppDefault {
  lazy val doSomething: ZIO[Any, Nothing, Long] =
    ZIO
      .debug("some long running task!")
      .repeat(Schedule.spaced(2.seconds))
      .onInterrupt(ZIO.debug("I've been interrupted"))

  val run =
    for {
      _ <- ZIO.debug("Starting the program!")
      fiber <- doSomething.fork
      _ <- ZIO.sleep(5.seconds)
      _ <- fiber.interrupt
      _ <- ZIO.debug("The fiber has been interrupted!")
    } yield ()
}
