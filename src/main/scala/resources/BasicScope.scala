package resources

import zio.*
import java.io.IOException

object BasicScope extends ZIOAppDefault:

  val open: IO[IOException, String] =
    Console.printLine("acquiring") *> ZIO.succeed("patata")

  def close(s: String): UIO[Unit] =
    Console.printLine(s"releasing $s").orDie

  val resource: ZIO[Scope, IOException, String] =
    ZIO.acquireRelease(open)(close)

  val workflow: ZIO[Scope, IOException, Int] = resource.flatMap { str =>
    ZIO.sleep(3.seconds) *>
      Console.printLine(s"using $str") *>
      ZIO.succeed(42)
  }

  val globalScope = workflow

  val localScope =
    for
      _ <- Console.printLine("begin program")
      _ <- ZIO.scoped {
        for
          _ <- Console.printLine("begin scope")
          _ <- workflow
          _ <- Console.printLine("end scope")
        yield ()
      }
      _ <- Console.printLine("end program")
    yield ()

  val run = globalScope
