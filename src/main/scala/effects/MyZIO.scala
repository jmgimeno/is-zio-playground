package effects

case class MyZIO[-R, +E, +A](unsafeRun: R => Either[E, A]):

  def map[B](f: A => B): MyZIO[R, E, B] =
    MyZIO { r =>
      unsafeRun(r) match
        case Left(e)  => Left(e)
        case Right(a) => Right(f(a))
    }

  def flatMap[R1 <: R, E1 >: E, B](f: A => MyZIO[R1, E1, B]): MyZIO[R1, E1, B] =
    MyZIO { r =>
      unsafeRun(r) match
        case Left(e)  => Left(e)
        case Right(a) => f(a).unsafeRun(r)
    }
