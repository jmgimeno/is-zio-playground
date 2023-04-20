package effects

import scala.io.StdIn

object MyIOExercises:

  val longComputation = MyIO {
    Thread.sleep(1000)
    42
  }

  val getCurrentTime: MyIO[Double] = ???

  def duration[A](ioa: MyIO[A]): MyIO[(A, Double)] = ???

  val readConsole: MyIO[String] = ???

  def writeConsole[A](a: A): MyIO[Unit] = ???

  val greet: MyIO[Unit] = ???

  @main def exercises() =
    ???
