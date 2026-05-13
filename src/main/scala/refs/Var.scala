package refs

import zio.*

trait Var[A]:
  def get: ZIO[Any, Nothing, A]
  def set(a: A): ZIO[Any, Nothing, Unit]
  def update(f: A => A): ZIO[Any, Nothing, Unit]

object Var:
  def make[A](a: A): ZIO[Any, Nothing, Var[A]] = ZIO.succeed {
    new:
      var a0 = a
      def get: ZIO[Any, Nothing, A] = ZIO.succeed { a0 }
      def set(a: A): ZIO[Any, Nothing, Unit] = ZIO.succeed { a0 = a }
      def update(f: A => A): ZIO[Any, Nothing, Unit] = ZIO.succeed { a0 = f(a0) }
  }

object VarBasic extends ZIOAppDefault:
  val makeVar = Var.make(42)
  val makeVar1 = makeVar
  val makeVar2 = makeVar

  val program =
    for
      v1 <- makeVar1
      v2 <- makeVar2
      _ <- v1.update(_ + 1)
      _ <- v2.update(_ + 1)
      l <- v1.get
      r <- v2.get
    yield (l, r)

  val run = program.debug

object VarProgramOneFiber extends ZIOAppDefault:

  val programOneFiber =
    for
      shared <- Var.make(0)
      _ <- ZIO.foreachDiscard(1 to 10_000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = programOneFiber.debug

object VarProgramManyFibers extends ZIOAppDefault:

  val programManyFibers =
    for
      shared <- Var.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10_000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = programManyFibers.debug
