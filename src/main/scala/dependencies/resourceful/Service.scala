package dependencies.resourceful

import zio.*

trait Service:
  def start: ZIO[Any, Nothing, Unit]
  def serve(m: String): ZIO[Any, Nothing, Unit]
  def shutdown: ZIO[Any, Nothing, Unit]

final case class ServiceLive() extends Service:
  def start = Console.printLine("starting service").orDie

  def serve(m: String) = Console.printLine(s"serving $m").orDie

  def shutdown = Console.printLine("shutdowning service").orDie

object ServiceLive:
  val layer: ZLayer[Any, Nothing, Service] =
    ZLayer.scoped {
      for
        http <- ZIO.succeed(ServiceLive())
        _ <- http.start
        _ <- ZIO.addFinalizer(http.shutdown)
      yield http
    }
