package dependencies

import zio.*

object MainWithLayers extends ZIOAppDefault:
  val run = ZIO
    .serviceWithZIO[BusinessLogic](_.run)
    .provide(
      BusinessLogicLive.layer,
      GithubLive.layer,
      HttpLive.layer
      // ZLayer.Debug.tree
      // ZLayer.Debug.mermaid
    )
