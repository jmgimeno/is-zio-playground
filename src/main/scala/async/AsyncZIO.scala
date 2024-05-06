import scala.io.StdIn

import zio.*

object AsyncZIO extends ZIOAppDefault:

  def readIntAsync(cb: Int => Unit): Unit =
    val n = StdIn.readInt()
    cb(n)

  def doubleAsync(n: Int)(cb: Int => Unit): Unit =
    cb(2 * n)

  val readIntZIO: ZIO[Any, Nothing, Int] =
    ZIO.async { callback =>
      readIntAsync { n =>
        callback(ZIO.succeed(n))
      }
    }

  def doubleZIO(n: Int) =
    ZIO.async { callback =>
      doubleAsync(n) { d =>
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
    _ <- ZIO.attempt(println(s"El resultado es $d"))
  yield ()
