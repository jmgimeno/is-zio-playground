package exitsandcauses

import zio.*
import zio.Cause.*

import java.lang

object ErrorHandling extends ZIOAppDefault {

  val zio1: ZIO[Any, String, Int] = ZIO.succeed(42)
  val zio2: ZIO[Any, String, Int] = ZIO.fail("patata")
  val zio3: ZIO[Any, String, Int] = ZIO.die(new UnsupportedOperationException("todo"))

  val run1 = zio3.foldZIO(
    str => Console.printLine(s"failed with $str"),
    int => Console.printLine(int * 2)
  )

  val run2 = zio3.sandbox.foldZIO(
    cause => { cause match
      case Fail(str,_) => Console.printLine(s"failed with $str")
      case Die(t, _) => Console.printLine("ha fallat")
    },
    int => Console.printLine(int * 2)
  )

  val run3 = zio3.foldCauseZIO(
    cause => {
      cause match
        case Fail(str, _) => Console.printLine(s"failed with $str")
        case Die(t, _) => Console.printLine("ha fallat")
    },
    int => Console.printLine(int * 2)
  )

  val zio4: ZIO[Any, Throwable, Int] = ZIO.fail(new UnsupportedOperationException("tomato"))

  private val zio5 : ZIO[Any, Nothing, Int]  = zio4.orDie

  val run = zio5.foldCauseZIO(
    e => Console.printLine(s"error $e"),
    int => Console.printLine(int * 5)
  )
}
