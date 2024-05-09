package callbacks

import zio.ZIO

object DirectZIO:

  def readInt(): ZIO[Any, Nothing, Int] =
    ZIO.succeed {
      Direct.readInt()
    }


  def double(n: Int): ZIO[Any, Nothing, Int] =
    ZIO.succeed {
      Direct.double(n)
    }

  @main def mainDZ(): Unit =
    for
      n <- readInt()
      d <- double(n)
      _ <- ZIO.succeed(println("El resultado es $d"))
    yield ()

