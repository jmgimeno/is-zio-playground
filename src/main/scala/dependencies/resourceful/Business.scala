package dependencies.resourceful

import zio.*

trait Business:
  def run: ZIO[Any, Nothing, Int]

final case class BusinessLive(service: Service) extends Business:
  def run =
    for
      _ <- Console.printLine("making business").!
      _ <- service.serve("42")
      _ <- Console.printLine("ending business").!
    yield 42

object BusinessLive:
  val layer =
    ZLayer.fromFunction(BusinessLive.apply)
