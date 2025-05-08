package errorhandling

import zio.{ZIO, ZIOAppDefault}

object Combining1 extends ZIOAppDefault {

  final case class ApiError(msg: String) extends Exception(msg)

  final case class DbError(msg: String) extends Exception(msg)

  lazy val callApi: ZIO[Any, ApiError, String] = ???
  lazy val queryDb: ZIO[Any, DbError, Int] = ???

  lazy val combine: ZIO[Any, Exception, (String, Int)] =
    callApi.zip(queryDb)

  lazy val combine2: ZIO[Any, ApiError | DbError, (String, Int)] =
    callApi.zip(queryDb)
    
  val run = ???

}
