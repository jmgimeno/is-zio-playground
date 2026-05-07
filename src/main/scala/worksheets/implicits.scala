@main def main(): Unit = {
  def f(a: Int)(b: Int) = a + b

  println(f(2)(3)) // parameteres are passed explicitly

  def g(a: Int)(using b: Int) = a * b // b is passed implicitly

  println(g(2)(using 3))

  given Int = 5

  println(g(2))

  object Local {

    given Int = 10

    def f(): Unit = {
      println(g(2))
    }
  }

  Local.f()
}



