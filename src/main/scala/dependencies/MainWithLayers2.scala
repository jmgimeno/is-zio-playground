package dependencies

import zio.*

object MainWithLayers2 extends ZIOAppDefault:

  val githubLayerNoDependencies: ZLayer[Any, Nothing, Github] =
    ZLayer.make[Github](
      GithubLive.layer,
      HttpLive.layer
      // ZLayer.Debug.tree
    )

  val run = ZIO
    .serviceWithZIO[BusinessLogic](_.run)
    .provide(
      githubLayerNoDependencies,
      BusinessLogicLive.layer
      // ZLayer.Debug.tree,
      // ZLayer.Debug.mermaid,
    )
