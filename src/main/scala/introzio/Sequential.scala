package introzio

import zio.*
import scala.io.StdIn

object Sequential extends ZIOAppDefault:

  val readLine: ZIO[Any, Throwable, String] =
    ZIO.attempt(StdIn.readLine())

  def printLine(line: String): ZIO[Any, Throwable, Unit] =
    ZIO.attempt(println(line))

  val echo: ZIO[Any, Throwable, Unit] =
    readLine.flatMap(line => printLine(line))

  val firstName: ZIO[Any, Throwable, String] =
    ZIO.attempt(StdIn.readLine("What is your first name? "))

  val lastName: ZIO[Any, Throwable, String] =
    ZIO.attempt(StdIn.readLine("What is your last name? "))

  val fullName: ZIO[Any, Throwable, String] =
    firstName.zipWith(lastName)((first, last) => s"$first $last")

  val helloWorld: ZIO[Any, Throwable, Unit] =
    ZIO.attempt(print("Hello, ")) *> ZIO.attempt(println("World!"))

  val printNumbers: ZIO[Any, Throwable, IndexedSeq[Unit]] =
    ZIO.foreach(1 to 100) { n =>
      printLine(n.toString)
    }

  val printNumbersNoVector: ZIO[Any, Throwable, Unit] =
    ZIO.foreachDiscard(1 to 100) { n =>
      printLine(n.toString)
    }

  val prints: List[ZIO[Any, Throwable, Unit]] =
    List(
      printLine("The"),
      printLine("quick"),
      printLine("brown"),
      printLine("fox")
    )

  val printWords: ZIO[Any, Throwable, List[Unit]] =
    ZIO.collectAll(prints)

  val printWordsNoList: ZIO[Any, Throwable, Unit] =
    ZIO.collectAllDiscard(prints)

  val run: ZIO[Any, Throwable, List[Unit]] = printWords.debug
