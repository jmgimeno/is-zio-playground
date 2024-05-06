package introzio

import scala.io.StdIn

import zio.*

object Constructors extends ZIOAppDefault:

  val readInt1: ZIO[Any, Throwable, Int] = ZIO.attempt(StdIn.readInt())

  val readInt2: ZIO[Any, Nothing, Int] = ZIO.succeed(StdIn.readInt())

  var run =
    readInt1.foldZIO(
      e => ZIO.attempt(println("Error")),
      _ => ZIO.attempt(println("Ok"))
    )
