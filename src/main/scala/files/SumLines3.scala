package files

import zio.*
import java.nio.file.*
import java.io.BufferedReader

object SumLines3 extends ZIOAppDefault:

  def sumLines(fname: String): ZIO[Any, Throwable, Int] =
    for
      file <- ZIO.attempt(
        Files.newBufferedReader(Paths.get("src", "main", "resources", fname))
      )
      sum <- sumLines(file)
      _ <- ZIO.attempt(file.close())
    yield sum

  def sumLines(file: BufferedReader): ZIO[Any, Throwable, Int] =
    def loop(sum: Int): ZIO[Any, Throwable, Int] =
      for
        line <- ZIO.attempt(file.readLine())
        sum <- if line ne null then loop(sum + line.toInt) else ZIO.succeed(sum)
      yield sum
    loop(0)

  val sumFile = for {
    fileName <- Console.readLine("File name? ")
    sum <- sumLines(fileName)
    _ <- Console.printLine(s"The sum is $sum")
  } yield ()

  val run = sumFile.catchAll {
    case _: NoSuchFileException   => Console.printLine("File does not exist")
    case _: NumberFormatException => Console.printLine("Some bad number")
  }
