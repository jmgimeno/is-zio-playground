package futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BasicFutures:

  @main def main(): Unit =
    val computation = Future(42)
    println(computation)
