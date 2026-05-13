package refs

import zio.*

object FiberRefTracingDemo extends ZIOAppDefault {

  final case class TraceCtx(requestId: String, span: Vector[String]) {
    def child(name: String): TraceCtx = copy(span = span :+ name)
    def render: String =
      s"req=$requestId span=${if (span.isEmpty) "root" else span.mkString("/")}"
  }

  trait Tracer {
    def log(msg: String): UIO[Unit]
    def inSpan[R, E, A](name: String)(zio: ZIO[R, E, A]): ZIO[R, E, A]
    def withRequestId[R, E, A](id: String)(zio: ZIO[R, E, A]): ZIO[R, E, A]
  }

  object Tracer {
    def log(msg: String): ZIO[Tracer, Nothing, Unit] =
      ZIO.serviceWithZIO[Tracer](_.log(msg))
    def inSpan[R, E, A](name: String)(
        zio: ZIO[R, E, A]
    ): ZIO[Tracer & R, E, A] =
      ZIO.serviceWithZIO[Tracer](_.inSpan(name)(zio))
    def withRequestId[R, E, A](id: String)(
        zio: ZIO[R, E, A]
    ): ZIO[Tracer & R, E, A] =
      ZIO.serviceWithZIO[Tracer](_.withRequestId(id)(zio))

    val live: ZLayer[Any, Nothing, Tracer] = ZLayer.scoped {
      FiberRef
        .make(
          TraceCtx("unknown", Vector.empty),
          (ctx: TraceCtx) => ctx, // fork inherit
          (parent: TraceCtx, _: TraceCtx) => parent // join keep parent
        )
        .map { ref =>
          new Tracer {
            def log(msg: String): UIO[Unit] =
              ref.get
                .flatMap(c => Console.printLine(s"[${c.render}] $msg").orDie)
            def inSpan[R, E, A](name: String)(zio: ZIO[R, E, A]): ZIO[R, E, A] =
              ref.locallyWith(_.child(name))(zio)
            def withRequestId[R, E, A](
                id: String
            )(zio: ZIO[R, E, A]): ZIO[R, E, A] =
              ref.locallyWith(_.copy(requestId = id, span = Vector.empty))(zio)
          }
        }
    }
  }

// No FiberRef arg, no TraceCtx arg:
  def dbCall: ZIO[Tracer, Nothing, String] =
    Tracer.inSpan("db")(Tracer.log("SELECT users").as("user-42"))

  def httpCall: ZIO[Tracer, Nothing, String] =
    Tracer.inSpan("http")(Tracer.log("GET /profile").as("ok"))

  def handleRequest(id: Int): ZIO[Tracer, Nothing, Unit] =
    Tracer.inSpan(s"request-$id") {
      for {
        _ <- Tracer.log("start")
        _ <- dbCall.zipPar(httpCall)
        _ <- Tracer.log("end")
      } yield ()
    }

  val program: ZIO[Tracer, Nothing, Unit] =
    for {
      rid1 <- Random.nextUUID.map(_.toString)
      rid2 <- Random.nextUUID.map(_.toString)

      f1 <- Tracer.withRequestId(rid1)(handleRequest(1)).fork
      f2 <- Tracer.withRequestId(rid2)(handleRequest(2)).fork

      _ <- f1.join.zipPar(f2.join)
    } yield ()

  val run: ZIO[Any, Any, Any] =
    program.provide(Tracer.live)
}
