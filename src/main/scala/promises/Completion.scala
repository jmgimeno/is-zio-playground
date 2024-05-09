package promises

import zio.*

object Completion extends ZIOAppDefault:

  val randomInt: UIO[Int] = Random.nextInt

  val complete: UIO[(Int, Int)] = for {
    p <- Promise.make[Nothing, Int]
    _ <- p.complete(randomInt)
    l <- p.await
    r <- p.await
  } yield (l, r)

  val completeWith: UIO[(Int, Int)] = for {
    p <- Promise.make[Nothing, Int]
    _ <- p.completeWith(randomInt)
    l <- p.await
    r <- p.await
  } yield (l, r)

  val run =
    complete.debug("complete")
      *> completeWith.debug("completeWith")
