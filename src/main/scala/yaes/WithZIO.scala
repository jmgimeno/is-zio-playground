package yaes

import zio.*

import java.net.URL

object WithZIO extends ZIOAppDefault {

  def fetchUrl(url: URL): ZIO[Any, Throwable, String] = ???

  def fetchAllUrlsPar(
      urls: List[URL]
  ): ZIO[Any, Nothing, (List[(URL, Throwable)], List[(URL, String)])] = {
    ZIO
      .foreachPar(urls) { url =>
        fetchUrl(url).mapBoth((url, _), (url, _)).either
      }
      .map(_.partitionMap(identity))
  }

  val drunkFlip: ZIO[Any, String, String] = {
    for {
      caught <- Random.nextBoolean
      heads <-
        if caught
        then Random.nextBoolean
        else ZIO.fail("we dropped the coin")
    } yield if heads then "Heads" else "Tails"
  }

  val run =
    drunkFlip
    .map(println)
    .catchAll(error => ZIO.succeed(println(s"Error: $error")))
}
