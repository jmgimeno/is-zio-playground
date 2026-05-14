package resources

import zio.*
import java.io.IOException

object Basic extends ZIOAppDefault:

  val open: IO[IOException, String] =
    Console.printLine("acquiring").as("patata")

  def close(s: String): UIO[Unit] =
    Console.printLine(s"releasing $s").orDie

  val workflow: IO[IOException, Int] =
    ZIO.acquireReleaseWith(open)(close) { resource =>
      Console.printLine(s"using $resource").delay(3.seconds).as(42)
    }

  val run = workflow.debug
