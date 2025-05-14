package fibers

import zio.*

object Basic extends ZIOAppDefault:

  def step[A](a: A): UIO[A] =
    ZIO.debug(s"begin $a")
      *> ZIO.sleep(500.millis)
      *> ZIO.succeed(a).debugThread
      <* ZIO.debug(s"end $a")

  lazy val program1: UIO[Unit] =
    for
      _ <- step(1)
      _ <- step(2)
      _ <- step(3)
    yield ()

  lazy val program2: UIO[Unit]  =
    for
      fib1 <- step(1).fork
      fib2 <- step(2).fork
      fib3 <- step(3).fork
      results <- (fib1 <*> fib2 <*> fib3).join
      _ <- Console.printLine(s"results $results").orDie
    yield ()

  lazy val program3: UIO[Unit]  =
    ZIO.foreachDiscard(1 to 10)(step)

  lazy val program4: UIO[Unit]  =
    ZIO.foreachParDiscard(1 to 10)(step)

  val run = program2
