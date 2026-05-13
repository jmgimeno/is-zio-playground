package refs

import java.util.concurrent.atomic.AtomicReference

import zio.*

trait Ref[A]:
  def modify[B](f: A => (B, A)): ZIO[Any, Nothing, B]
  def get: ZIO[Any, Nothing, A] = modify(a => (a, a))
  def set(a: A): ZIO[Any, Nothing, Unit] = modify(_ => ((), a))
  def update[B](f: A => A): ZIO[Any, Nothing, Unit] = modify(a => ((), f(a)))

object Ref:
  def make[A](a: A): ZIO[Any, Nothing, Ref[A]] =
    ZIO.succeed {
      new:
        val atomic = new AtomicReference(a)
        def modify[B](f: A => (B, A)): UIO[B] =
          ZIO.succeed {
            var loop = true
            var b: B = null.asInstanceOf[B]
            while (loop) {
              val current = atomic.get
              val tuple = f(current)
              b = tuple._1
              loop = !atomic.compareAndSet(current, tuple._2)
            }
            b
          }
    }

object RefProgram extends ZIOAppDefault:

  val programBad =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10000) { _ =>
        shared.get.flatMap(n => shared.set(n + 1))
      }
      result <- shared.get
    yield result

  val program =
    for
      shared <- Ref.make(0)
      _ <- ZIO.foreachParDiscard(1 to 10000) { _ =>
        shared.update(_ + 1)
      }
      result <- shared.get
    yield result

  val run = programBad.debug
