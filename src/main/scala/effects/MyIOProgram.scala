package effects

object MyIOProgram:

  def twice =
    MyIO(println("Hello"))
      .flatMap(_ => MyIO(println("Hello")))

  def alsoTwice =
    val hello = MyIO(println("Hello"))
    hello.flatMap(_ => hello)

  @main def run() =
    twice.unsafeRun()
