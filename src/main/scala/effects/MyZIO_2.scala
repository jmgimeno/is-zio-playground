package effects

import scala.util.control.NonFatal

// This version uses folds on eithers instead of match
// You can reason about the code by following the types.

case class MyZIO_2[-R, +E, +A](unsafeRun: R => Either[E, A]):

  self =>

  def map[B](f: A => B): MyZIO_2[R, E, B] =
    MyZIO_2 { r =>
      self.unsafeRun(r).map(f)
    }

  def flatMap[R1 <: R, E1 >: E, B](
      f: A => MyZIO_2[R1, E1, B]
  ): MyZIO_2[R1, E1, B] =
    MyZIO_2 { r =>
      self.unsafeRun(r).fold(MyZIO_2.fail(_), f).unsafeRun(r)
    }

object MyZIO_2:

  def attempt[A](a: => A): MyZIO_2[Any, Throwable, A] =
    MyZIO_2 { _ =>
      try Right(a)
      catch {
        case t if NonFatal(t) => Left(t)
      }
    }

  def fail[E](e: => E): MyZIO_2[Any, E, Nothing] =
    MyZIO_2 { _ =>
      Left(e)
    }
