package effects

case class MyZIO[-R, +E, +A](run: R => Either[E, A]):

  self =>

  def map[B](f: A => B): MyZIO[R, E, B] =
    MyZIO { r =>
      run(r) match
        case Left(e)  => Left(e)
        case Right(a) => Right(f(a))
    }

  def flatMap[R1 <: R, E1 >: E, B](f: A => MyZIO[R1, E1, B]): MyZIO[R1, E1, B] =
    MyZIO { r =>
      self.run(r) match
        case Left(e)  => Left(e)
        case Right(a) => f(a).run(r)
    }
