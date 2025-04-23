package effects

import scala.io.StdIn

object MyIOExercises:

  private val longComputation = MyIO {
    Thread.sleep(1000)
    42
  }

  val getCurrentTime: MyIO[Double] =
    ???

  def duration[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    ???

  def durationFor[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    ???

  val readConsole: MyIO[String] =
    ???

  def writeConsole[A](a: A): MyIO[Unit] =
    ???

  // Execució / NO Transparència referencial
  def greetImperative(): Unit =
    println("Entra el teu nom")
    val name = StdIn.readLine()
    println(s"Hola $name")

  // Descripció / Transparèncoa referencial
  val greet: MyIO[Unit] =
    ???

  @main def duration(): Unit = {
    val computation: MyIO[(Int, Double)] = durationFor(longComputation)
    val withPrinting: MyIO[Unit] = computation.flatMap(writeConsole)
    //// ^^^^ transparència referencial
    /// =============================================
    //// vvvv no tenim transparència referencial
    withPrinting.unsafeRun()
  }

  @main def greeting(): Unit =
    greet.unsafeRun()

  @main def patates(): Unit =
    val onePatata = MyIO(println("patata"))
    val tenPatata = onePatata.repeat(9)
    tenPatata.unsafeRun()

