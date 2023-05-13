package fibers

import zio.*

object Basic extends ZIOAppDefault:

  def step[A](a: A) =
    (ZIO.debug(s"begin $a")
      *> ZIO.sleep(500.millis)
      *> ZIO.succeed(a)
      <* ZIO.debug(s"end $a")).debugThread

  val program1 =
    for
      _ <- step(1)
      _ <- step(2)
      _ <- step(3)
    yield ()

  val program2 =
    for
      f1 <- step(1).fork
      f2 <- step(2).fork
      f3 <- step(3).fork
    // _ <- (f1 <*> f2 <*> f3).join
    yield ()

  val program3 =
    ZIO.foreach(1 to 10)(step)

  val program4 =
    ZIO.foreachPar(1 to 20)(step)

  val run = program1.debugThread
