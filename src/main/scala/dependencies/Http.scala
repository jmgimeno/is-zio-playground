package dependencies

import zio.*

trait Http:
  def get(url: String): ZIO[Any, Throwable, Chunk[Byte]]
  def post(url: String, body: Chunk[Byte]): ZIO[Any, Throwable, Chunk[Byte]]

final case class HttpLive() extends Http:
  def get(url: String): ZIO[Any, Throwable, Chunk[Byte]] =
    ???
  def post(url: String, body: Chunk[Byte]): ZIO[Any, Throwable, Chunk[Byte]] =
    ???

object HttpLive:
  val layer: ZLayer[Any, Nothing, Http] =
    ZLayer.succeed(HttpLive())
