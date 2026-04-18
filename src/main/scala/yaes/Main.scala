package yaes

import zio.*

object Main extends ZIOAppDefault {

  // Create to ZIO trait with get and set methods
  trait FirstGetSet {
    def get: UIO[Option[String]]
    def set(value: String): UIO[Unit]
  }

  trait SecondGetSet {
    def get: UIO[Option[String]]
    def set(value: String): UIO[Unit]
  }

  final case class FirstGetSetImpl(ref: FiberRef[Option[String]])
    extends FirstGetSet {
    override def get: UIO[Option[String]] = ref.get
    override def set(value: String): UIO[Unit] = ref.set(Some(value))
  }

  final case class SecondGetSetImpl(ref: FiberRef[Option[String]])
    extends SecondGetSet {
    override def get: UIO[Option[String]] = ref.get
    override def set(value: String): UIO[Unit] = ref.set(Some(value))
  }

  val liveFirst: URLayer[FiberRef[Option[String]], FirstGetSetImpl] =
    ZLayer.fromFunction(FirstGetSetImpl.apply)
  val liveSecond: URLayer[FiberRef[Option[String]], SecondGetSetImpl] =
    ZLayer.fromFunction(SecondGetSetImpl.apply)

  override def run = {
    val app = for {
      firstGetSet <- ZIO.service[FirstGetSet]
      secondGetSet <- ZIO.service[SecondGetSet]
      _ <- firstGetSet.set("FirstGetSet")
      firstValue <- firstGetSet.get
      secondValue <- secondGetSet.get
      _ = println(
        s"FirstGetSet value: $firstValue"
      ) // [info] FirstGetSet value: Some(FirstGetSet)
      _ = println(
        s"SecondGetSet value: $secondValue"
      ) // [info] SecondGetSet value: Some(FirstGetSet)
    } yield ()

    app
      .provideSome[Scope](
        liveFirst,
        liveSecond,
        ZLayer(FiberRef.make(Option.empty[String])).fresh
      )
  }
}
