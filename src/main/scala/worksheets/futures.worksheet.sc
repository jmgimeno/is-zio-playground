import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.*

def comp1 = Future {
  println(Thread.currentThread().getName())
  println("Begin future 1")
  Thread.sleep(500);
  println("End future 2")
  42
}

val c1 = comp1
val c2 = comp1

println(Thread.currentThread().getName())

Await.ready(c1, 1.second)
Await.ready(c2, 1.second)


