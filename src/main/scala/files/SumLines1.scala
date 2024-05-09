package files

import zio.*
import java.nio.file.*

object SumLines1 extends ZIOAppDefault:

  def sumLines(fname: String): ZIO[Any, Nothing, Int] =
    ZIO.succeed {
      val file =
        Files.newBufferedReader(Paths.get("src", "main", "resources", fname))
      var line: String = file.readLine()
      var sum: Int = 0
      while line ne null do
        sum += line.toInt
        line = file.readLine()
      file.close()
      sum
    }

  val sumFile = for {
    fileName <- Console.readLine("File name? ")
    sum <- sumLines(fileName)
    _ <- Console.printLine(s"The sum is $sum")
  } yield ()

  val run = sumFile
