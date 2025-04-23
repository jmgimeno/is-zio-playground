package effects

object MyIOProgram:

  def twice: MyIO[Unit] =
    MyIO(println("Hello"))
      .flatMap(_ => MyIO(println("Hello")))

  def alsoTwice: MyIO[Unit] =
    val hello = MyIO(println("Hello"))
    hello.flatMap(_ => hello)

  def alsoAlsoTwice: MyIO[Unit] =
    val hello = MyIO(println("Hello"))
    hello.repeat(2)

  @main def run(): Unit =
    alsoTwice.unsafeRun()
