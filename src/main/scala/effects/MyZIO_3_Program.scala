package effects

import scala.io.StdIn

object MyZIO_3_Program {

  val factorial: MyZIO_3[Int, Nothing, Int] =
    MyZIO_3 { n =>
      Right((1 to n).product)
    }

  def readInt(prompt: String): MyZIO_3[Any, Nothing, Int] =
    MyZIO_3 { _ =>
      print(prompt)
      Right(scala.io.StdIn.readInt())
    }

  val program1: MyZIO_3[Any, Nothing, Unit] = for {
    n <- readInt("Enter an integer: ")
    f <- factorial.provide(n)
  } yield println(s"factorial of $n is $f")

  @main def runProgram1(): Unit =
    program1.unsafeRun(())

  val program2: MyZIO_3[Int, Nothing, Unit] = for {
    f <- factorial
    n <- MyZIO_3.environment
    _ <- MyZIO_3.succeed(println(s"factorial of $n is $f"))
  } yield ()

  @main def runProgram2(): Unit = {
    // Guarrada a efected d'explicar el que passa
    print("Enter an integer: ")
    val n = StdIn.readInt()
    program2.unsafeRun(n)
  }
}
