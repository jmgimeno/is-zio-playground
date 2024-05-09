package effects

import zio.*
import java.io.IOException

object Repetition extends ZIOAppDefault:

  val program: ZIO[Any, IOException, Unit] =
    for
      name <- Console.readLine("Ask? ")
      _ <-
        if (name == "patata")
        then ZIO.unit
        else program
    yield ()

  val run = program
