package introzio

import zio.*

// This mechanism is uses for DEPENDENCY INJECTION

object Environment extends ZIOAppDefault:

  trait MyLogger:
    def log(s: String): ZIO[Any, Nothing, Unit]

  // The real ZIO.environment returns a ZEnvironment
  // we must use get to access what is inside

  val anotherLogged: ZIO[MyLogger, Nothing, Unit] =
    for
      logger <- ZIO.environment[MyLogger]
      _ <- logger.get.log("another")
    yield ()

  val logged: ZIO[MyLogger, Nothing, Unit] =
    for
      _ <- anotherLogged
      _ <- ZIO.attempt(println("Hola")).orDie
      _ <- anotherLogged
    yield ()

  // provideEnvironment uses a ZEnvironment
  // we pass an anonymous instance of MyLogger

  def run =
    logged.provideEnvironment(
      ZEnvironment(_ =>
        ZIO.succeed(())
      ) // s => ZIO.attempt(println(s"log: $s")).orDie)
    )
