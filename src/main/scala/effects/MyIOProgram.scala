package effects

object MyIOProgram:

  val twice: MyIO[Unit] =
    MyIO(println("Hello"))
      *> MyIO(println("Hello"))

  val twiceViaFor: MyIO[Unit] =
    for
      _ <- MyIO(println("Hello"))
      _ <- MyIO(println("Hello"))
    yield ()

  val twiceViaFlatMapAndMap: MyIO[Unit] =
    MyIO(println("Hello")).flatMap { _ =>
      MyIO(println("Hello")).map { _ =>
        ()
      }
    }

  val alsoTwice: MyIO[Unit] =
    val hello = MyIO(println("Hello"))
    hello *> hello

  @main def run(): Unit =
    twiceViaFor.repeatN(5).unsafeRun()
