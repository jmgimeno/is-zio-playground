package promises

import zio.*

object Basic extends ZIOAppDefault:

  val program =
    for
      promise <- Promise.make[Nothing, Unit]
      left <-
        (Console.printLine("Hello, ")
          *> ZIO.sleep(2.seconds)
          *> promise.succeed(())).fork
      right <-
        (promise.await
          *> Console.printLine(" World!")).fork
      _ <- left.join *> right.join
    yield ()

  val run = program
