package effects

import scala.io.StdIn

object MyIOExercises:

  private val longComputation = MyIO {
    Thread.sleep(1000)
    42
  }

  val getCurrentTime: MyIO[Double] =
    MyIO(System.currentTimeMillis())

  def duration[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    getCurrentTime.flatMap { start =>
      ioa.flatMap { a =>
        getCurrentTime.map { end =>
          (a, end - start)
        }
      }
    }

  def durationFor[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    for {
      start <- getCurrentTime
      a <- ioa
      end <- getCurrentTime
    } yield (a, end - start)

  lazy val readConsole: MyIO[String] =
    MyIO(scala.io.StdIn.readLine())

  def writeConsole[A](a: A): MyIO[Unit] =
    MyIO(println(a))

  // Execució / NO Transparència referencial
  def greetImperative(): Unit =
    println("Entra el teu nom")
    val name = StdIn.readLine()
    println(s"Hola $name")

  // Descripció / Transparèncoa referencial
  lazy val greet: MyIO[Unit] =
    for {
      _ <- writeConsole("Entra el teu nom: ")
      name <- readConsole
      _ <- writeConsole(s"Hola $name")
    } yield ()


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

  def factorial(n: Int): MyIO[Int] = MyIO((1 to n).product)

  @main def factorialRun(): Unit = {
    (for {
      input <- readConsole
      n = input.toInt
      f <- factorial(n).when(n > 5)(-1)
      _ <- writeConsole(f)
    } yield()).unsafeRun()
  }
