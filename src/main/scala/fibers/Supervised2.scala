package fibers

import zio.*

object Supervised2 extends ZIOAppDefault:
  val child: UIO[Unit] =
    (Console.printLine("Child fiber beginning execution...").orDie *>
      ZIO.sleep(5.seconds) *>
      Console.printLine("Hello from a child fiber!").orDie)
      .onInterrupt(Console.printLine("child interrupted").orDie)

  val parent: UIO[Unit] =
    (Console.printLine("Parent fiber beginning execution...").orDie *>
      child.fork *>
      ZIO.sleep(3.seconds) *>
      Console.printLine("Hello from a parent fiber!").orDie)
      .onInterrupt(Console.printLine("parent interrupted").orDie)

  val example: UIO[Unit] = for
    fiber <- parent.fork
    _ <- ZIO.sleep(1.second)
    _ <- fiber.interrupt
    _ <- ZIO.sleep(10.seconds)
  yield ()

  val run = example
