package effects

import scala.io.StdIn

object MyIOExercises:

  val longComputation = MyIO {
    Thread.sleep(1000)
    42
  }

  val getCurrentTime: MyIO[Double] =
    new MyIO[Double](() => System.currentTimeMillis())

  val getCurrentTime2: MyIO[Double] =
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
    for
      start <- getCurrentTime
      a <- ioa
      end <- getCurrentTime
    yield (a, end - start)

  val readConsole: MyIO[String] =
    MyIO(StdIn.readLine())

  def writeConsole[A](a: A): MyIO[Unit] =
    MyIO(println(a))

  // Descripció / Transparèncoa referencial
  val greet: MyIO[Unit] =
    for
      _ <- writeConsole("Entra el teu nom")
      name <- readConsole
      _ <- writeConsole(s"Hola $name")
    yield ()

  // Execució / NO Transparència referencial
  def greetImperative(): Unit =
    println("Entra el teu nom")
    val name = StdIn.readLine()
    println(s"Hola $name")

  val greet2: MyIO[Unit] =
    for
      _ <- MyIO(println("Entra el teu nom"))
      name <- MyIO(StdIn.readLine())
      _ <- MyIO(println(s"Hola $name"))
    yield ()

  @main def duration(): Unit = {
    val computation: MyIO[(Int, Double)] = durationFor(longComputation)
    val withPrinting: MyIO[Unit] = computation.flatMap(writeConsole)
    //// ^^^^ transparència referencial
    /// =============================================
    //// vvvv no tenim transparència referencial
    withPrinting.unsafeRun()
  }

  @main def greeting(): Unit =
    greet2.unsafeRun()

  @main def patates(): Unit =
    val onePatata = MyIO(println("patata"))
    val tenPatata = onePatata.repeat(9)
    tenPatata.unsafeRun()

