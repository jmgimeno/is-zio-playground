import scala.io.StdIn

import zio.*

object AsyncZ extends ZIOAppDefault:

  def readIntSync(): Int =
    val n = StdIn.readInt()
    return n

  def readIntAsync[B](cb: Int => B): B =
    val n = StdIn.readInt()
    cb(n)

  def doubleSync(n: Int): Int =
    return 2 * n

  def doubleAsync[B](n: Int)(cb: Int => B): B =
    cb(2 * n)

  val readIntZ: ZIO[Any, Nothing, Int] =
    ZIO.async { callback =>
      readIntAsync { n =>
        callback(ZIO.succeed(n))
      }
    }

  def doubleZ(n: Int) =
    ZIO.async { callback =>
      doubleAsync(n) { d =>
        callback(ZIO.succeed(d))
      }
    }

  // @main def mainZ =
  //   readIntAsync { n =>
  //     doubleAsync(n) { d =>
  //       println(s"El resultado es $d")
  //     }
  //   }

  val run = for
    n <- readIntZ
    d <- doubleZ(n)
    _ <- ZIO.attempt(println(s"El resultado es $d"))
  yield ()
