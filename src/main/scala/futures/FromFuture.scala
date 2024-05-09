package futures

import zio.*
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object FromFuture extends ZIOAppDefault:

  def goShoppingFuture(using ec: ExecutionContext): Future[Unit] =
    Future(println("Going to the grocery store"))

  val goShoppingZIO: Task[Unit] =
    ZIO.fromFuture(ec => goShoppingFuture(using ec))

  val run = goShoppingZIO
