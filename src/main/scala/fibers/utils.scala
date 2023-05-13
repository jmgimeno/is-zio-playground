package fibers

import zio.*

extension [R, E, A](zio: ZIO[R, E, A])
  def debugThread: ZIO[R, E, A] =
    val threadName = Thread.currentThread().getName()
    for
      fiberId <- ZIO.fiberId
      name = s"[fiber-${fiberId.id} @ $threadName]"
      a <- zio
        .tap(a => ZIO.succeed(println(s"$name SUCCEED: $a")))
        .tapErrorCause(cause => ZIO.succeed(println(s"$name FAILED: $cause")))
    yield a
