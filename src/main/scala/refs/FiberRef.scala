package refs

import zio.*

object FibParallelWithLogCtx extends ZIOAppDefault {

  // We don't use a FiberRef and we maintain context explicitly
  // We mix logging and computation logic.

  final case class LogCtx(path: Vector[String]) {
    def left: LogCtx = copy(path = path :+ "L")
    def right: LogCtx = copy(path = path :+ "R")
    def render: String = if (path.isEmpty) "root" else path.mkString("/")
  }

  type Logs = Vector[String]

  private def line(ctx: LogCtx, msg: String): String =
    s"[path=${ctx.render}] $msg"

  // We return the result and the logs of the computation
  def fib(n: Int, ctx: LogCtx): ZIO[Any, Nothing, (RuntimeFlags, Logs)] =
    if (n <= 1) {
      val logs = Vector(line(ctx, s"fib($n) = $n"))
      ZIO.succeed((n, logs))
    } else {
      val start = line(ctx, s"start fib($n)")
      for {
        (a, logsA, (b, logsB)) <- fib(n - 1, ctx.left).zipPar(
          fib(n - 2, ctx.right)
        )
        r = a + b
        done = line(ctx, s"done fib($n) = $r")
      } yield (r, Vector(start) ++ logsA ++ logsB ++ Vector(done))
    }

  val run: ZIO[Any, Any, Unit] =
    for {
      n <- ZIO.succeed(6)
      out <- fib(n, LogCtx(Vector.empty))
      (res, logs) = out
      _ <- Console.printLine(s"fib($n) = $res")
      _ <- ZIO.foreachDiscard(logs)(Console.printLine(_))
    } yield ()
}

object FibParallelWithFiberRef extends ZIOAppDefault {

  // We use two FiberRefs, one for the path and one for the log
  type Path = Vector[String]
  type Logs = Vector[String]

  // This fiber ref only passes information from parent to child
  // the child doesn't modify it and it doesn't affect the parent
  // Even the child gets a different value because the locallyWith
  // function is used to create a new fiberRef with a different value

  val pathRefZIO: ZIO[Scope, Nothing, FiberRef[Path]] =
    FiberRef.make[Path](
      initial = Vector.empty,
      fork = identity,
      join = (parent, _) => parent
    )

  val logsRefZIO: ZIO[Scope, Nothing, FiberRef[Logs]] =
    FiberRef.make[Logs](
      initial = Vector.empty,
      fork = _ => Vector.empty,
      join = (parent, child) => parent ++ child
    )

  private def render(path: Path): String =
    if (path.isEmpty) "root" else path.mkString("/")

  private def log(
      pathRef: FiberRef[Path],
      logsRef: FiberRef[Logs],
      msg: String
  ): ZIO[Any, Nothing, Unit] =
    for {
      path <- pathRef.get
      _ <- logsRef.update(_ :+ s"[path=${render(path)}] $msg")
    } yield ()

  // Computation only returns value and the log is updated in the fiberRef

  def fib(
      n: Int,
      pathRef: FiberRef[Path],
      logsRef: FiberRef[Logs]
  ): ZIO[Any, Nothing, Int] =
    if (n <= 1) {
      log(pathRef, logsRef, s"fib($n) = $n").as(n)
    } else {
      for {
        _ <- log(pathRef, logsRef, s"start fib($n)")
        leftF <- pathRef
          .locallyWith(_ :+ "L")(fib(n - 1, pathRef, logsRef))
          .fork
        rightF <- pathRef
          .locallyWith(_ :+ "R")(fib(n - 2, pathRef, logsRef))
          .fork
        a <- leftF.join
        b <- rightF.join
        r = a + b
        _ <- log(pathRef, logsRef, s"done fib($n) = $r")
      } yield r
    }

  val run: ZIO[Any, Any, Unit] =
    ZIO.scoped {
      for {
        n <- ZIO.succeed(6)
        pathRef <- pathRefZIO
        logsRef <- logsRefZIO
        res <- fib(n, pathRef, logsRef)
        logs <- logsRef.get
        _ <- Console.printLine(s"fib($n) = $res")
        _ <- ZIO.foreachDiscard(logs)(Console.printLine(_))
      } yield ()
    }
}

object FibExplicitPathFiberRefLogs extends ZIOAppDefault {

  type Logs = Vector[String]

  val logsRefZIO: ZIO[Scope, Nothing, FiberRef[Logs]] =
    FiberRef.make[Logs](
      initial = Vector.empty,
      fork = _ => Vector.empty, // cada hijo acumula sus logs
      join = (parent, child) => parent ++ child
    )

  private def render(path: Vector[String]): String =
    if (path.isEmpty) "root" else path.mkString("/")

  private def log(
      logsRef: FiberRef[Logs],
      path: Vector[String],
      msg: String
  ): ZIO[Any, Nothing, Unit] =
    logsRef.update(_ :+ s"[path=${render(path)}] $msg")

  def fib(
      n: Int,
      path: Vector[String],
      logsRef: FiberRef[Logs]
  ): ZIO[Any, Nothing, RuntimeFlags] =
    if (n <= 1)
      log(logsRef, path, s"fib($n) = $n").as(n)
    else
      for {
        _ <- log(logsRef, path, s"start fib($n)")
        (a, b) <- fib(n - 1, path :+ "L", logsRef)
          .zipPar(fib(n - 2, path :+ "R", logsRef))
        r = a + b
        _ <- log(logsRef, path, s"done fib($n) = $r")
      } yield r

  val run: ZIO[Any, Any, Unit] = {
    ZIO.scoped {
      for {
        logsRef <- logsRefZIO
        n = 6
        result <- fib(n, Vector.empty, logsRef)
        logs <- logsRef.get
        _ <- Console.printLine(s"fib($n) = $result")
        _ <- Console.printLine("---- logs ----")
        _ <- ZIO.foreachDiscard(logs)(Console.printLine(_))
      } yield ()
    }
  }
}
