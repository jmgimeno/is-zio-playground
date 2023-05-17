package resources

import zio.*
import java.io.IOException

object Basic extends ZIOAppDefault:

  val open: IO[IOException, String] =
    Console.printLine("acquiring") *> ZIO.succeed("patata")

  def close(s: String): UIO[Unit] =
    Console.printLine(s"releasing $s").orDie

  val workflow: IO[IOException, Int] =
    ZIO.acquireReleaseWith(open)(close) { resource =>
      ZIO.sleep(3.seconds) *>
        Console.printLine(s"using $resource") *>
        ZIO.succeed(42)
    }

  val run = workflow.debug
