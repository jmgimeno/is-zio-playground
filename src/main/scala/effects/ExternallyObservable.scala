package effects

enum List[+A]:
  case Nil
  case Cons(head: A, tail: List[A])

  @annotation.tailrec
  final def foldLeft[B](z: B)(f: (B, A) => B): B = this match
    case Nil => z
    case Cons(x, xs) => xs.foldLeft(f(z, x))(f)

  def foldRight[B](z: B)(f: (A, B) => B): B = this match
    case Nil => z
    case Cons(x, xs) => f(x, xs.foldRight(z)(f))

  def foldRight_TR[B](z: B)(f: (A, B) => B): B =
    reversed.foldLeft(z)((b, a) => f(a, b))

  def reversed: List[A] =
    foldLeft(Nil)((xs, x) => Cons(x, xs))

object List:
  def apply[A](as: A*): List[A] =
    if as.isEmpty then Nil else Cons(as.head, apply(as.tail*))

@main def unobservable(): Unit = {
  val l = List(1, 2, 3)
  val left = l.foldLeft(0)(_ + _)
  println(s"With foldLeft $left")
  val right = l.foldRight(0)(_ + _)
  println(s"With foldRight $right")
  val right_TR = l.foldRight_TR(0)(_ + _)
  println(s"With foldRight_TR $right_TR")
}

def spy(n: Int): () => Int =
  () => {
    println(n)
    n
  }

@main def observable(): Unit = {
  val l = List(spy(1), spy(2), spy(3))
  val left = l.foldLeft(0)((acc, f) => acc + f())
  println(s"With foldLeft $left")
  val right = l.foldRight(0)((f, acc) => f() + acc)
  println(s"With foldRight $right")
  val right_TR = l.foldRight_TR(0)((f, acc) => f() + acc)
  println(s"With foldRight_TR $right_TR")
}