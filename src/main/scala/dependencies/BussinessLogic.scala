package dependencies

import zio.*

trait BusinessLogic:
  def run: ZIO[Any, Throwable, Unit]

final case class BusinessLogicLive(github: Github) extends BusinessLogic:
  val run: ZIO[Any, Throwable, Unit] =
    for
      issues <- github.getIssues("zio")
      comment = Comment("I am working on this!")
      _ <- ZIO.getOrFail(issues.headOption).flatMap { issue =>
        github.postComment(issue, comment)
      }
    yield ()

object BusinessLogicLive:
  val layer: ZLayer[Github, Nothing, BusinessLogic] =
    ZLayer.fromFunction(BusinessLogicLive(_))
