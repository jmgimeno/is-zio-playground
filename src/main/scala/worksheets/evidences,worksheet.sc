import zio.ZIO

// Read docs for type <:<

case class MyError(msg: String)

case class MyException(msg: String) extends Throwable(msg)

val workflow1 : ZIO[Any, MyError, Int] = ZIO.fail(MyError("error"))

val workflow2 : ZIO[Any, MyException, Int] = ZIO.fail(MyException("exception"))

def fun1[R, E, A](effect: ZIO[R, E, A]): ZIO[Any, Nothing, Int] = ZIO.succeed(42)

fun1(workflow1)
fun1(workflow2)

def fun2[R, E <: Throwable, A](effect: ZIO[R, E, A]): ZIO[Any, Nothing, Int] = ZIO.succeed(42)

// fun2(workflow1)
fun2(workflow2)

def fun3[R, E, A](effect: ZIO[R, E, A])(using ev: E <:< Throwable): ZIO[Any, Nothing, Int] =
  ZIO.succeed(42)

// fun2(workflow1)
fun2(workflow2)
