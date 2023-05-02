package introzio

import scala.io.StdIn

import zio.*

object Echo extends ZIOAppDefault:

  val readLine: ZIO[Any, Throwable, String] =
    ZIO.attempt(StdIn.readLine())

  def printLine(line: String): ZIO[Any, Throwable, Unit] =
    ZIO.attempt(println(line))

  val echo = for
    line <- readLine
    _ <- printLine(line)
  yield ()

  val run = echo
