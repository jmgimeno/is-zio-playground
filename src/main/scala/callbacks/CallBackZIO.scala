package callbacks

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

//  @main def main(): Unit =
//    readIntCallBack { n =>
//      doubleCallBack(n) { d =>
//        println(s"El resultado es $d")
//      }
//    }

  val run: ZIO[Any, Nothing, Unit] = for
    n <- readIntZIO
    d <- doubleZIO(n)
    _ <- ZIO.succeed(println(s"Result is $d"))
  yield ()
