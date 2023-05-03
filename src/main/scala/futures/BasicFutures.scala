package futures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.ExecutionContext.global

object BasicFutures:
  @main def main() =
    val computation = Future(42)
    println(computation)
