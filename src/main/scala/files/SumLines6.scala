package files

import zio.*

import java.io.BufferedReader
import java.nio.file.*

object SumLines6 extends ZIOAppDefault:

  def sumLines(fname: String): ZIO[Any, Throwable, Int] =
    for
      file <- ZIO.attempt(
        Files.newBufferedReader(Paths.get("src", "main", "resources", fname))
      )
      sum <- sumLines(file)
      _ <- ZIO.attempt(file.close())
    yield sum

  def sumLines(file: BufferedReader): ZIO[Any, Throwable, Int] =
    var sum = 0 // Safe cause the mutation will be delayed, and var is not shared
                // sum is not RT buts its modifications inside a ZIO are
                // Can use a Ref to make it RT
    def loop: ZIO[Any, Throwable, Unit] =
      for
        line <- ZIO.attempt(file.readLine())
        _ <-
          (for
            num <- ZIO.attempt(line.toInt).orElseSucceed(0)
            _ <- ZIO.attempt(sum += num)
            _ <- loop
          yield ()).when(line ne null)
      yield ()
    loop.as(sum)

  val sumFile = for {
    fileName <- Console.readLine("File name? ")
    sum <- sumLines(fileName)
    _ <- Console.printLine(s"The sum is $sum")
  } yield ()

  val run = sumFile.catchAll { case _: NoSuchFileException =>
    Console.printLine("File does not exist")
  }
