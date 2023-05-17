package refs

import zio.*

trait Var[A]:
  def get: UIO[A]
  def set(a: A): UIO[Unit]
  def update(f: A => A): UIO[Unit]

object Var:
  def make[A](a: A): UIO[Var[A]] = ZIO.succeed {
    new:
      var a0 = a
      def get: UIO[A] = ZIO.succeed(a0)
      def set(a: A): UIO[Unit] = ZIO.succeed {
        a0 = a
        ()
      }
      def update(f: A => A): UIO[Unit] = ZIO.succeed { a0 = f(a0) }
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

object VarProgram extends ZIOAppDefault:

  val program =
    for
      shared <- Var.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = program.debug
