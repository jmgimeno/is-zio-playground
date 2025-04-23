package effects

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.*

object FuturesProgram:

  def twice: Future[Unit] =
    Future(println("Hello"))
      .flatMap(_ => Future(println("Hello")))

  def notTwice: Future[Unit] =
    val hello = Future(println("Hello"))
    hello.flatMap(_ => hello)

  def factorial(n: Int): Int = (1 to n).product

  def factorialAsync(n: Int): Future[Int] = Future(factorial(n))

  def doubleAsync(n: Int): Future[Int] = Future(n * 2)

  def factorialOfDouble(n: Int): Future[Int] =
    doubleAsync(n).flatMap(d => factorialAsync(d))

  @main def main(): Unit =
    Await.ready(notTwice, 1.second)
