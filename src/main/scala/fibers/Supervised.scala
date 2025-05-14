package fibers

import zio.*

object Supervised extends ZIOAppDefault:
  lazy val barJob: ZIO[Any, Nothing, Long] =
    ZIO
      .debug("Bar: still running!")
      .repeat(Schedule.fixed(1.seconds))

  lazy val fooJob: ZIO[Any, Nothing, Unit] =
    for {
      _ <- ZIO.debug("Foo: started!")
      _ <- barJob.fork
      _ <- ZIO.sleep(3.seconds)
      _ <- ZIO.debug("Foo: finished!")
    } yield ()

  val run =
    for
      f <- fooJob.fork
      _ <- f.join
    yield ()
