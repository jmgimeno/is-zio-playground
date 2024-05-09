package refs

import zio.*

object RealRef extends ZIOAppDefault:

  val programBad =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10000) { _ =>
        for
          v <- shared.get
          _ <- shared.set(v + 1)
        yield ()
      }
      result <- shared.get
    yield result

  val program =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = program.debug
