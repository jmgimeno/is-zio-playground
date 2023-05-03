import scala.io.StdIn

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import zio.*
import scala.util.Failure
import scala.util.Success
import java.util.concurrent.CountDownLatch

object AsyncF extends ZIOAppDefault:

  def readIntAsync(cb: Int => Unit): Unit =
    Future {
      val n = StdIn.readInt()
      Thread.sleep(500)
      n
    }.onComplete {
      case Failure(exception) => println("Error in readInt")
      case Success(value)     => cb(value)
    }

  def doubleAsync(n: Int)(cb: Int => Unit): Unit =
    Future {
      val r = 2 * n
      Thread.sleep(500)
      r
    }.onComplete {
      case Failure(exception) => println("Error in double")
      case Success(value)     => cb(value)
    }

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

  // @main def mainF =
  //   val latch = new CountDownLatch(1)
  //   readIntAsync { n =>
  //     doubleAsync(n) { d =>
  //       println(s"El resultado es $d")
  //       latch.countDown()
  //     }
  //   }
  //   latch.await()

  val run = for
    n <- readIntZ
    d <- doubleZ(n)
    _ <- ZIO.attempt(println(s"El resultado es $d"))
  yield ()
