package fibers

import zio.*

object ForkDaemonExample extends ZIOAppDefault {
  
  lazy val healthChecker: ZIO[Any, Nothing, Long] =
    ZIO
      .debug("Checking the health of the system...")
      .repeat(Schedule.spaced(1.second))
      .onInterrupt(ZIO.debug("Health checker interrupted!"))

  lazy val parent: ZIO[Any, Nothing, Unit] = {
    for {
      _ <- ZIO.debug("Parent fiber begins execution...")
      _ <- healthChecker.forkDaemon
      _ <- ZIO.sleep(5.seconds)
      _ <- ZIO.debug("Shutting down the parent fiber!")
    } yield ()
  }.onInterrupt(ZIO.debug("Parent fiber interrupted"))

  val run = for {
    fiber <- parent.fork
    _ <- ZIO.sleep(1.seconds)
    _ <- fiber.interrupt
    _ <- ZIO.sleep(10.seconds)
  } yield ()
}
