package effects

import scala.util.control.NonFatal

// A little refactoring of MyZIO_2 simplifying the code for flatMap
// You can reason about the refactoring by substitution (referential transparency).

case class MyZIO_3[-R, +E, +A](unsafeRun: R => Either[E, A]):

  self =>

  def map[B](f: A => B): MyZIO_3[R, E, B] =
    MyZIO_3 { 
      r => self.unsafeRun(r).map(f)
    }

  def flatMap[R1 <: R, E1 >: E, B](f: A => MyZIO_3[R1, E1, B]): MyZIO_3[R1, E1, B] =
    MyZIO_3 { r =>
      self.unsafeRun(r).fold(Left.apply, f(_).unsafeRun(r))
    }
    
  def provide(r: => R): MyZIO_3[Any, E, A] =
    MyZIO_3 { _ =>
      self.unsafeRun(r)
    }
    
object MyZIO_3:

  def attempt[A](a: => A): MyZIO_3[Any, Throwable, A] =
    MyZIO_3 { _ =>
      try Right(a)
      catch {
        case t if NonFatal(t) => Left(t)
      }
    }

  def fail[E](e: => E): MyZIO_3[Any, E, Nothing] =
    MyZIO_3 { 
      _ => Left(e)
    }
    
  def environment[R]: MyZIO_3[R, Nothing, R] =
    MyZIO_3(Right.apply)

  def succeed[A](a: => A): MyZIO_3[Any, Nothing, A] =
    MyZIO_3(_ => Right(a))
    