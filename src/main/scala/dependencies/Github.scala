package dependencies

import zio.*

trait Github:
  def getIssues(organization: String): ZIO[Any, Throwable, Chunk[Issue]]
  def postComment(issue: Issue, comment: Comment): ZIO[Any, Throwable, Unit]

final case class GithubLive(http: Http) extends Github:
  def getIssues(organization: String): ZIO[Any, Throwable, Chunk[Issue]] =
    ???

  def postComment(issue: Issue, comment: Comment): ZIO[Any, Throwable, Unit] =
    ???

object GithubLive:
  val layer: ZLayer[Http, Nothing, Github] =
    ZLayer.fromFunction(GithubLive(_))
