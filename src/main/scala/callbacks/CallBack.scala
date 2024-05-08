import scala.io.StdIn

object CallBack:

  def readIntCallBack[A](cb: Int => A): A =
    print("Enter a number: ")
    val n = StdIn.readInt()
    cb(n)

  def doubleCallBack[A](n: Int)(cb: Int => A): A =
    cb(2 * n)

  @main def main() =
    readIntCallBack { n =>
      doubleCallBack(n) { d =>
        println(s"El resultado es $d")
      }
    }
