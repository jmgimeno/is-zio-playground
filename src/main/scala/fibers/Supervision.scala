package fibers

import zio.*

object Supervision extends ZIOAppDefault:
  val child: UIO[Unit] =
    Console.printLine("Child fiber beginning execution...").orDie *>
      ZIO.sleep(5.seconds) *>
      Console.printLine("Hello from a child fiber!").orDie

  val parent: UIO[Unit] =
    Console.printLine("Parent fiber beginning execution...").orDie *>
      child.fork *>
      ZIO.sleep(3.seconds) *>
      Console.printLine("Hello from a parent fiber!").orDie

  val example: UIO[Unit] = for
    fiber <- parent.fork
    _ <- ZIO.sleep(1.second)
    _ <- fiber.interrupt
    _ <- ZIO.sleep(10.seconds)
  yield ()

  val run = example
