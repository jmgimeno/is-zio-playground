package dependencies.resourceful

import zio.*

object Main extends ZIOAppDefault:

  val run =
    ZIO
      .serviceWithZIO[Business](_.run)
      .provide(
        BusinessLive.layer,
        ServiceLive.layer
      )
