package resources

import zio.*
import java.io.IOException

object BasicScope extends ZIOAppDefault:

  val open: IO[IOException, String] =
    Console.printLine("acquiring").as("patata")

  def close(s: String): UIO[Unit] =
    Console.printLine(s"releasing $s").orDie

  val resource: ZIO[Scope, IOException, String] =
    ZIO.acquireRelease(open)(close)

  val workflow: ZIO[Scope, IOException, Int] = resource.flatMap { str =>
    Console.printLine(s"using $str").delay(3.seconds).as(42)
  }

  val globalScope: ZIO[Scope, IOException, Int] = workflow

  val localScope: ZIO[Any, IOException, Unit] =
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
