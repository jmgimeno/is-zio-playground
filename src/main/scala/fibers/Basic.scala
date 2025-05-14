package fibers

import zio.*

object Basic extends ZIOAppDefault:

  def step[A](a: A): UIO[A] =
    (ZIO.debug(s"begin $a")
      *> ZIO.sleep(500.millis)
      *> ZIO.succeed(a)
      <* ZIO.debug(s"end $a")).debugThread

  lazy val program1 =
    for
      _ <- step(1)
      _ <- step(2)
      _ <- step(3)
    yield ()

  lazy val program2 =
    for
      fib1 <- step(1).fork
      fib2 <- step(2).fork
      fib3 <- step(3).fork
      results <- (fib1 <*> fib2 <*> fib3).join
      _ <- Console.printLine(s"results $results")
    yield ()

  lazy val program3 =
    ZIO.foreach(1 to 10)(step)

  lazy val program4 =
    ZIO.foreachPar(1 to 20)(step)

  val run = program2
