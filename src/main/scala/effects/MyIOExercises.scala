package effects

import scala.io.StdIn

object MyIOExercises:

  val longComputation = MyIO {
    Thread.sleep(1000)
    42
  }

  val getCurrentTime: MyIO[Double] = MyIO(System.currentTimeMillis())

  def duration[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    getCurrentTime.flatMap(startTime =>
      ioa.flatMap(a =>
        getCurrentTime.map(endTime =>
          val timeTaken = endTime - startTime
          (a, timeTaken)
        )
      )
    )

  def durationViaForComprehension[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    for
      startTime <- getCurrentTime
      a <- ioa
      endTime <- getCurrentTime
    yield (a, endTime - startTime)

  def durationOnlyFlatmap[A](ioa: MyIO[A]): MyIO[(A, Double)] =
    getCurrentTime.flatMap(startTime =>
      ioa.flatMap(a =>
        getCurrentTime.flatMap(endTime =>
          val timeTaken = endTime - startTime
          MyIO((a, timeTaken))
        )
      )
    )

  val readConsole: MyIO[String] = MyIO(StdIn.readLine())

  def writeConsole[A](a: A): MyIO[Unit] = MyIO(println(a))

  val greet: MyIO[Unit] =
    for
      _ <- writeConsole("Com et dius? ")
      name <- readConsole
      _ <- writeConsole(s"Hola, $name")
    yield ()

  def greetImperative(): Unit =
    println("Com et dius? ")
    val name = StdIn.readLine()
    println(s"Hola, $name")

  @main def exercises() =
    greet.repeatN(3).unsafeRun()

    // duration(longComputation)
    //   .flatMap { case (a, t) =>
    //     MyIO(println(s"Result: $a Time: $t"))
    //   }
    //   .unsafeRun()

    // Aquí mai ens sortim del paradigma funcional fins el DARRER MOMENT (la
    // crida al unsafeRun). Per tant, el programa, la construcció de l'expressió
    // que el representa està dins del paradigma funcional

    // getCurrentTime
    //   .flatMap { time =>
    //     MyIO(println(s"Time: $time"))
    //   }
    //   .unsafeRun()

    // El problema del codi següent és que "fem coses" després d'haver-nos
    // sortit del paradigma funcional (fem coses DESPRÉS del unsafeRun)

    // println(getCurrentTime.unsafeRun())
