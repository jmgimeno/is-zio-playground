package callbacks

import zio.*

object CallBackZIO extends ZIOAppDefault:

  val readIntZIO: ZIO[Any, Nothing, Int] =
    ZIO.async { callback =>
      CallBack.readIntCallBack({ n =>
        callback(ZIO.succeed(n))
      })
    }

  def doubleZIO(n: Int): ZIO[Any, Nothing, Int] =
    ZIO.async { callback =>
      CallBack.doubleCallBack(n)({ d =>
        callback(ZIO.succeed(d))
      })
    }

//  @main def main(): Unit =
//    readIntCallBack { n =>
//      doubleCallBack(n) { d =>
//        println(s"El resultado es $d")
//      }
//    }

  val run: ZIO[Any, Nothing, Unit] = for
    n <- readIntZIO
    d <- doubleZIO(n)
    _ <- ZIO.succeed(println(s"Double of $n is $d"))
  yield ()
