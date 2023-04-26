package introzio

import zio.*
import scala.io.StdIn

object Sequential extends ZIOAppDefault:

  val readLine = ZIO.attempt(StdIn.readLine())

  def printLine(line: String) = ZIO.attempt(println(line))

  val echo = readLine.flatMap(line => printLine(line))

  val firstName =
    ZIO.attempt(StdIn.readLine("What is your first name? "))

  val lastName =
    ZIO.attempt(StdIn.readLine("What is your last name? "))

  val fullName =
    firstName.zipWith(lastName)((first, last) => s"$first $last")

  val helloWorld =
    ZIO.attempt(print("Hello, ")) *> ZIO.attempt(println("World!"))

  val printNumbers =
    ZIO.foreach(1 to 100) { n =>
      printLine(n.toString)
    }

  val prints =
    List(
      printLine("The"),
      printLine("quick"),
      printLine("brown"),
      printLine("fox")
    )

  val printWords =
    ZIO.collectAll(prints)

  def run = ???
