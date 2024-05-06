import scala.io.StdIn

object Async:

  def readIntAsync(cb: Int => Unit): Unit =
    val n = StdIn.readInt()
    cb(n)

  def doubleAsync(n: Int)(cb: Int => Unit): Unit =
    cb(2 * n)

  @main def main() =
    readIntAsync { n =>
      doubleAsync(n) { d =>
        println(s"El resultado es $d")
      }
    }

