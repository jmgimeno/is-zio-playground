package dependencies

import zio.*

object MainWithLayers extends ZIOAppDefault:
  val run = ZIO
    .serviceWithZIO[BusinessLogic](_.run)
    .provide(
      HttpLive.layer,
      GithubLive.layer,
      BusinessLogicLive.layer,
      // ZLayer.Debug.tree,
      // ZLayer.Debug.mermaid
    )
