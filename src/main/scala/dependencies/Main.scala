package dependencies

import zio.*

object Main extends ZIOAppDefault:
  val http: Http = HttpLive()
  val github: Github = GithubLive(http)
  val businessLogic: BusinessLogic = BusinessLogicLive(github)

  val run = businessLogic.run
