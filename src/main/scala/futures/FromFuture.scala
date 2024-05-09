package futures

import zio.*
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object FromFuture extends ZIOAppDefault:

  def goShoppingFuture(using ec: ExecutionContext): Future[Unit] =
    Future(println("Going to the grocery store"))

  val goShoppingZIO: ZIO[Any, Throwable, Unit] =
    ZIO.fromFuture(ec => goShoppingFuture(using ec))
  
  val run: ZIO[Any, Throwable, Unit] = 
    goShoppingZIO
