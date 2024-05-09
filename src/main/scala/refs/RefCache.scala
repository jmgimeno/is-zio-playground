package refs

import zio.*

trait RefCache[K, V]:
  def getOrElseCompute(k: K)(f: K => V): UIO[Ref[V]]

object RefCache:
  def make[K, V]: UIO[RefCache[K, V]] =
    Ref.Synchronized.make(Map.empty[K, Ref[V]]).map { ref =>
      new RefCache[K, V] {
        def getOrElseCompute(k: K)(f: K => V): UIO[Ref[V]] =
          ref.modifyZIO { map =>
            map.get(k) match {
              case Some(ref) =>
                ZIO.succeed((ref, map))
              case None =>
                Ref.make(f(k)).map(ref => (ref, map + (k -> ref)))
            }
          }
      }
    }

object RefCacheProgram extends ZIOAppDefault:

  val program =
    for
      cache <- RefCache.make[String, Int]
      refV <- cache.getOrElseCompute("patata")(_.length)
      v <- refV.get
      _ <- Console.printLine(s"value= $v")
      _ <- refV.set(42)
      refV2 <- cache.getOrElseCompute("patata")(_.length)
      v2 <- refV2.get
      _ <- Console.printLine(s"value= $v2")
    yield ()

  val run = program
