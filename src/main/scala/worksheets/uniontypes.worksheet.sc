import scala.util.Random

def fun(): Int | String =
  if Random.nextBoolean() then 10 else "patata"

val result = fun()

var res = result match
  case r: Int => r * 4
  case s: String => s.length
