import scala.io.StdIn

import zio.*

object CallBackZIO extends ZIOAppDefault:

  import CallBack.*

  val readIntZIO: ZIO[Any, Nothing, Int] =
    ZIO.async { callback =>
      readIntCallBack { n =>
        callback(ZIO.succeed(n))
      }
    }

  def doubleZIO(n: Int): ZIO[Any, Nothing, Int] =
    ZIO.async { callback =>
      doubleCallBack(n) { d =>
        callback(ZIO.succeed(d))
      }
    }

  // @main def main =
  //   readIntAsync { n =>
  //     doubleAsync(n) { d =>
  //       println(s"El resultado es $d")
  //     }
  //   }

  val run = for
    n <- readIntZIO
    d <- doubleZIO(n)
    _ <- ZIO.attempt(println(s"Result is $d"))
  yield ()
