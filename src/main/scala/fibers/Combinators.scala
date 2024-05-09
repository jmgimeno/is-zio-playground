package fibers

import zio.*
import java.io.IOException

object Combinators extends ZIOAppDefault:

  def effect(a: Int, d: Duration): IO[Unit, Int] =
    (Console.printLine(s"Begin $a").orDie
      *> ZIO.fail(()).when(a == 0)
      *> ZIO.sleep(d)
      *> ZIO.succeed(a)
      <* Console.printLine(s"End $a").orDie)
      .onInterrupt(Console.printLine(s"Interrupted $a").orDie)

  val a = effect(1, 5.seconds)
  val b = effect(0, 7.seconds)

  val sequential =
    for result <- a.zip(b)
    yield result

  val parallel =
    for result <- a.zipPar(b)
    yield result

  val race =
    for result <- a.raceEither(b)
    yield result

  val collectAllPar: ZIO[Any, Unit, IndexedSeq[Int]] =
    val effects = (0 to 10).map(effect(_, 1.second))
    ZIO.collectAllPar(effects)

  val foreachParError =
    ZIO.foreachPar(List(0, 2, 0, 3, 4))(effect(_, 500.millis))

  val validateParError =
    ZIO.validatePar(List(0, 2, 0, 3, 4))(effect(_, 500.millis))

  val run = validateParError.debug
