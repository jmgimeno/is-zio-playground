package effects

class MyIO[A] private (val unsafeRun: () => A):

  def map[B](f: A => B): MyIO[B] =
    new MyIO(() => f(unsafeRun()))

  // def map[B](f: A => B): MyIO[B] =
  //   flatMap(a => MyIO(f(a)))

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    new MyIO(() =>
      val nextIO = f(unsafeRun())
      nextIO.unsafeRun()
    )

  def *>[B](iob: => MyIO[B]): MyIO[B] =
    flatMap(_ => iob)

  def repeatN(n: Int): MyIO[A] =
    if (n == 0) then this
    else this *> repeatN(n - 1)

object MyIO:
  def apply[A](a: => A): MyIO[A] =
    new MyIO(() => a)
