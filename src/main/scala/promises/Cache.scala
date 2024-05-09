package promises

import zio.*

trait Cache[-K, +E, +V]:
  def get(key: K): IO[E, V]

object Cache:
  def make[K, E, V](
      lookup: K => IO[E, V]
  ): UIO[Cache[K, E, V]] =
    for {
      ref <- Ref.make[Map[K, Promise[E, V]]](Map.empty)
    } yield new:
      def get(key: K): IO[E, V] =
        Promise.make[E, V].flatMap { newPromise =>
          ref
            .modify { map =>
              map.get(key) match {
                case Some(oldPromise) =>
                  (Right(oldPromise), map)
                case None =>
                  (Left(newPromise), map + (key -> newPromise))
              }
            }
            .flatMap {
              case Right(oldPromise) =>
                oldPromise.await
              case Left(newPromise) =>
                newPromise.complete {
                  lookup(key)
                } *> newPromise.await
            }
        }

object CacheProgram extends ZIOAppDefault:

  val program =
    for
      cache <- Cache.make[String, Nothing, Int] { key =>
        Console.printLine(s"Computing $key").orDie
          *> ZIO.sleep(1.seconds)
          *> ZIO.succeed(key.length)
          <* Console.printLine(s"Computed $key").orDie
      }
      getPatata =
        for
          _ <- Console.printLine("About to get patata")
          v <- cache.get("patata")
          _ <- Console.printLine(s"value= $v")
        yield v
      result <- ZIO.foreachPar(1 to 5)(_ => getPatata)
    yield result

  val run = program.debug
