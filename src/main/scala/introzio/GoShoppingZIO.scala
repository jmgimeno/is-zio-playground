package introzio

import zio.*

object GoShoppingZIO extends ZIOAppDefault:

  val goShopping =
    ZIO.attempt(println("Going to the grocery store"))

  val goShoppingLater =
    goShopping.delay(10.seconds)

  val run =
    for
      _ <- ZIO.debug("before")
      _ <- goShoppingLater
      _ <- ZIO.debug("after")
    yield ()
