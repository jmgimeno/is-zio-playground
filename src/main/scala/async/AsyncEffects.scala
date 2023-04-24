package async

import zio.*
import scala.concurrent.ExecutionContext.global

object AsyncEffects extends ZIOAppDefault:

  def timesTwo(n: Int)(callback: Int => Unit): Unit =
    global.execute { () =>
      val result = 2 * n
      callback(result)
    }

  def timesTwoZIO(n: Int): ZIO[Any, Nothing, Int] =
    ZIO.async { register =>
      timesTwo(n) { r =>
        register(ZIO.succeed(r))
      }
    }

  val run1 = ZIO.succeed {
    timesTwo(20) { r =>
      println(s"Result $r")
    }
  }

  val run2 = timesTwoZIO(20).flatMap { r =>
    Console.printLine(s"Result from ZIO $r")
  }

  val run =
    for
      n <- ZIO.succeed(20)
      r <- timesTwoZIO(n)
      _ <- Console.printLine(s"Result from FOR $r")
    yield ()
