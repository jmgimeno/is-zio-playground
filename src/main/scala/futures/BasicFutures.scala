package futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object BasicFutures:

  @main def main(): Unit =
    val computation = Future(42)
    println(computation)
