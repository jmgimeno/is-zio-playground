package effects

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesProgram:

  def twice =
    Future(println("Hello"))
      .flatMap(_ => Future(println("Hello")))

  def notTwice =
    val hello = Future(println("Hello"))
    hello.flatMap(_ => hello)

  @main def main() =
    twice.map(_ => ())
