package dependencies

import zio.*

trait Github:
  def getIssues(organization: String): ZIO[Any, Throwable, Chunk[Issue]]
  def postComment(issue: Issue, comment: Comment): ZIO[Any, Throwable, Unit]

final case class GithubLive(http: Http) extends Github:

  def getIssues(organization: String): ZIO[Any, Throwable, Chunk[Issue]] = {
    ZIO.debug(s"about to get issue in $organization using http") *>
      http
        .get(s"url of ${organization}")
        .as(Chunk(Issue(1), Issue(2)))
  }

  def postComment(issue: Issue, comment: Comment): ZIO[Any, Throwable, Unit] = {
    ZIO.debug(s"about to post comment $comment on $issue using http") *>
      http
        .post(s"url of ${issue}", Chunk.fromArray(comment.text.getBytes))
        .unit
  }

object GithubLive:
  val layer: ZLayer[Http, Nothing, Github] =
    ZLayer.fromFunction(GithubLive(_))
