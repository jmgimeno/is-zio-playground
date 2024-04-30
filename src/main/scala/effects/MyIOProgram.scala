package effects

object MyIOProgram:

  def twice: MyIO[Unit] =
    MyIO(println("Hello"))
      .flatMap(_ => MyIO(println("Hello")))

  def alsoTwice: MyIO[Unit] =
    val hello = MyIO(println("Hello"))
    hello.flatMap(_ => hello)

  @main def run() =
    alsoTwice.unsafeRun()
