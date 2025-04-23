package effects

object MyIOProgram:

  val twice: MyIO[Unit] = {
    MyIO(println("Hello"))
      .flatMap(_ => MyIO(println("Hello")))
  }

  val alsoTwice: MyIO[Unit] = {
    val hello = MyIO(println("Hello"))
    hello.flatMap(_ => hello)
  }

  def twiceImperative(): Unit = {
    println("hello")
    println("hello")
  }

  val twiceWithFor: MyIO[Unit] = {
    for {
      _ <- MyIO(println("hello"))
      _ <- MyIO(println("hello"))
    } yield()
  }

  lazy val twiceWithRepeat: MyIO[Unit] = {
    val hello = MyIO(println("Hello"))
    hello.repeat(2)
  }

  @main def run(): Unit = {
    twiceWithRepeat.unsafeRun()
  }
  
  
