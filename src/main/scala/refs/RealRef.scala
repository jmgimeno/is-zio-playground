package refs

import zio.*

object RefProgramOneFiber extends ZIOAppDefault:

  val programOneFiber =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachDiscard(1 to 10_000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = programOneFiber.debug

object RefProgramManyFibers extends ZIOAppDefault:

  val programManyFibers =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10_000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = programManyFibers.debug

object RefBadProgramManyFibers extends ZIOAppDefault:

  val programManyFibers =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10_000) { _ =>
        for {
          n <- shared.get
          _ <- shared.set(n + 1)
        } yield ()
      }
      result <- shared.get
    yield result

  val run = programManyFibers.debug
