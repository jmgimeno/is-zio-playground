package introzio

import scala.io.StdIn

import zio.*

object Constructors extends ZIOAppDefault:

  val readInt1: ZIO[Any, Nothing, Int] = ZIO.succeed(StdIn.readInt())

  val readInt2: ZIO[Any, Throwable, Int] = ZIO.attempt(StdIn.readInt())

  var run =
    readInt2.foldZIO(
      e => ZIO.attempt(println("Error")),
      _ => ZIO.attempt(println("Ok"))
    )
