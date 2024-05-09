package callbacks

import scala.io.StdIn

object Direct:

  def readInt(): Int =
    print("Enter a number: ")
    StdIn.readInt()
  
  def double(n: Int): Int =
    2 * n

  @main def mainD(): Unit =
    val n = readInt()
    val d = double(n)
    println(s"Double of $n is $d")
