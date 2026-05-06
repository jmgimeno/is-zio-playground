package errorhandling

import zio.*

object Shifting extends ZIOAppDefault {

  trait DbError

  trait UserProfile {
    def age: Int
  }

  def lookupProfile(userId: String): ZIO[Any, DbError, Option[UserProfile]] = ???
  
//  val workflow1 =
//    for {
//      user1 <- lookupProfile("u1")
//      user2 <- lookupProfile("u2")
//    } yield user1.age + user2.age
//  
  
  def lookupProfile2(userId: String): ZIO[Any, Option[DbError], UserProfile] =
    lookupProfile(userId).some
    
  val workflow2: ZIO[Any, Option[DbError], Int] =
    for {
      user1 <- lookupProfile2("u1")
      user2 <- lookupProfile2("u2")
    } yield user1.age + user2.age
    
  val workflow3: ZIO[Any, DbError, Int] =
    workflow2.foldZIO(
      e => e.fold(ZIO.succeed(-1))(ZIO.fail(_)),
      v => ZIO.succeed(v)
    )
    
  val run = ???
}
