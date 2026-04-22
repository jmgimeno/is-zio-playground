package effects

object MyZIOProgram:

  val factorialProgram: MyZIO[Int, Nothing, Int] =
    MyZIO { n =>
      Right((1 to n).product)
    }

  @main def runProgram(): Unit = {
    val program = for {
      f <- factorialProgram
      _ <- MyZIO(n => Right(println(s"factorial of $n is $f")))
    } yield()
    program.unsafeRun(5)
  }



