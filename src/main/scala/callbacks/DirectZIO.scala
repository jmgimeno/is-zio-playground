package callbacks

import zio.{ZIO, ZIOAppDefault}

object DirectZIO extends ZIOAppDefault:

  val readInt: ZIO[Any, Nothing, Int] =
    ZIO.succeed {
      Direct.readInt()
    }


  def double(n: Int): ZIO[Any, Nothing, Int] =
    ZIO.succeed {
      Direct.double(n)
    }

  val run: ZIO[Any, Nothing, Unit] =
    for
      n <- readInt
      d <- double(n)
      _ <- ZIO.succeed(println(s"Double of $n is $d"))
    yield ()

