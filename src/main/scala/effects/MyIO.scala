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
    if n > 1 then this.flatMap(_ => repeat(n - 1))
    else this

  def when(condition: Boolean)(default: => A): MyIO[A] =
    if condition then this else MyIO(default)
    
object MyIO:
  def apply[A](a: => A): MyIO[A] =
    new MyIO(() => a)
