package effects

import scala.io.StdIn

object MyIOExercises:

  private val longComputation = MyIO {
    Thread.sleep(1000)
    42
  }

  val getCurrentTime: MyIO[Double] =
    MyIO {
      System.currentTimeMillis()
    }

  def duration[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    getCurrentTime
      .flatMap(begin =>
        ioa
          .flatMap(a =>
            getCurrentTime
              .map(end =>
                (a, end - begin)
              )
          )
      )

  def durationFor[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    for {
      begin <- getCurrentTime
      a <- ioa
      end <- getCurrentTime
    } yield (a, end - begin)

  def readConsole(prompt: String): MyIO[String] =
    MyIO {
      scala.io.StdIn.readLine(prompt)
    }

  def writeConsole[A](a: A): MyIO[Unit] =
    MyIO {
      println(a)
    }

  // Execució / NO Transparència referencial
  def greetImperative(): Unit = {
    val name = scala.io.StdIn.readLine("Com et dius? ")
    println(s"Hola $name")
  }

  // Descripció / Transparèncoa referencial
  lazy val greet: MyIO[Unit] =
    for {
      name <- readConsole("Com et dius? ")
      _ <- writeConsole(s"Hola $name")
    } yield ()


  @main def duration(): Unit = {
    val program = for {
      (res, dur) <- duration(longComputation)
      _ <- MyIO {
        println(s"Result: $res and duration $dur")
      }
    } yield ()
    program.unsafeRun()
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
      input <- readConsole("")
      n = input.toInt
      f <- factorial(n).when(n > 5)(-1)
      _ <- writeConsole(f)
    } yield()).unsafeRun()
  }
