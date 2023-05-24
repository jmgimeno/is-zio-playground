package scopes

import zio.*

object Basics extends ZIOAppDefault:

  val acquisition: ZIO[Any, Nothing, Int] =
    Console.printLine("acquiring").orDie
      *> ZIO.succeed(42)

  def release(n: Int): ZIO[Any, Nothing, Unit] =
    Console.printLine(s"releasing $n").orDie

  val resource: ZIO[Scope, Nothing, Int] =
    ZIO.acquireRelease(acquisition)(release)

  // Utilitzem el global scope
  // val run = resource.debug("resource")

  // Utilitzant un scope local

  val run =
    for
      _ <- Console.printLine("first")
      _ <- ZIO.scoped {
        resource.debug("local scoped")
      }
      _ <- Console.printLine("last")
    yield ()
