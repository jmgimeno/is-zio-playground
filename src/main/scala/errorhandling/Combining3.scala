package errorhandling

import zio.{ZIO, ZIOAppDefault}

object Combining3 extends ZIOAppDefault {

  final case class ApiError(msg: String)

  final case class DbError(msg: String)

  lazy val callApi: ZIO[Any, ApiError, String] = ???
  lazy val queryDb: ZIO[Any, DbError, Int] = ???

  lazy val combine: ZIO[Any, Any, (String, Int)] =
    callApi.zip(queryDb)

  lazy val combine2: ZIO[Any, Either[ApiError, DbError], (String, Int)] =
    callApi
      .mapError(Left(_))
      .zip(queryDb.mapError(Right(_)))

  val run = ???

}
