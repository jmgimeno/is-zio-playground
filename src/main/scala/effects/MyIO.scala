package effects

class MyIO[A](val unsafeRun: () => A):

  def map[B](f: A => B): MyIO[B] =
    new MyIO(() => f(unsafeRun()))

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    new MyIO(() =>
      val nextIO = f(unsafeRun())
      nextIO.unsafeRun()
    )

  def repeat(n: Int): MyIO[A] =
    if (n == 0) then this
    else this.flatMap(_ => repeat(n - 1))

object MyIO:
  def apply[A](a: => A): MyIO[A] =
    new MyIO(() => a)
