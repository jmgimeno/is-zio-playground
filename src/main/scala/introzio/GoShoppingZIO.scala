package introzio

import zio.*

object GoShoppingZIO extends ZIOAppDefault:

  val goShopping: ZIO[Any, Throwable, Unit] =
    ZIO.attempt(println("Going to the grocery store"))

  val goShoppingLater: ZIO[Any, Throwable, Unit] =
    goShopping.delay(5.seconds)

  val run: ZIO[Any, Throwable, Unit] =
    for
      _ <- ZIO.attempt(println("before"))
      _ <- goShoppingLater
      _ <- ZIO.attempt(println("after"))
    yield ()
