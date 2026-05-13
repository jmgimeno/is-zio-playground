package refs

import zio.*

object RefSynchronized extends ZIOAppDefault {

  def updateAndLogBad[A](ref: Ref[A])(f: A => A): ZIO[Any, Nothing, (A, A)] =
    ref
      .modify { oldValue =>
        val newValue = f(oldValue)
        println(s"updated $oldValue to $newValue") // Side effect !!! NONONO !!!
        ((oldValue, newValue), newValue)
      }

  val programBad = for {
    ref <- Ref.make(0)
    _ <- ZIO.foreachParDiscard(1 to 10) { _ =>
      updateAndLogBad(ref)(_ + 1)
    }
    v <- ref.get
    _ <- Console.printLine(s"final value is $v").orDie
  } yield ()

  def updateAndLogAtEnd[A](ref: Ref[A])(f: A => A): ZIO[Any, Nothing, Unit] =
    ref
      .modify { oldValue =>
        val newValue = f(oldValue)
        ((oldValue, newValue), newValue)
      }
      .flatMap { case (oldValue, newValue) =>
        Console.printLine(s"updated $oldValue to $newValue").orDie
      }

  val programOk = for {
    ref <- Ref.make(0)
    _ <- ZIO.foreachParDiscard(1 to 10) { _ =>
      updateAndLogAtEnd(ref)(_ + 1)
    }
    v <- ref.get
    _ <- Console.printLine(s"final value is $v").orDie
  } yield ()

  def updateAndLog[A](
      ref: Ref.Synchronized[A]
  )(f: A => A): ZIO[Any, Nothing, (A, A)] =
    ref
      .modifyZIO { oldValue =>
        val newValue = f(oldValue)
        Console
          .printLine(s"updated $oldValue to $newValue")
          .orDie
          .as {
            ((oldValue, newValue), newValue)
          }
      }

  val programSynchronized = for {
    ref <- Ref.Synchronized.make(0)
    _ <- ZIO.foreachParDiscard(1 to 10) { _ =>
      updateAndLog(ref)(_ + 1)
    }
    v <- ref.get
    _ <- Console.printLine(s"final value is $v").orDie
  } yield ()

  val run = programSynchronized
}
