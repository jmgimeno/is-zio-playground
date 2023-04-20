package effects

class MyIO[A](val unsafeRun: () => A):

  def map[B](f: A => B): MyIO[B] =
    new MyIO(() => f(unsafeRun()))

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    new MyIO(() =>
      val nextIO = f(unsafeRun())
      nextIO.unsafeRun()
    )

object MyIO:
  def apply[A](a: => A): MyIO[A] =
    new MyIO(() => a)
