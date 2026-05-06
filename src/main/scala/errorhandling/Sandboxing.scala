package errorhandling

import zio.*

object Sandboxing extends ZIOAppDefault {

  val program1: ZIO[Any, Throwable, Int] =
    ZIO.attempt(1 / 0)

  val program2: ZIO[Any, Nothing, Int] =
    ZIO.succeed(1 / 0)

  val sandbox1: ZIO[Any, Cause[Throwable], Int] = program1.sandbox

  val sandbox2: ZIO[Any, Cause[Nothing], Int] = program2.sandbox

  val treat1 =
    program1.foldZIO(
      e => Console.printLine("Error: " + e),
      v => Console.printLine("Value: " + v)
    )

//  val treat2 =
//    program2.foldZIO(
//      e => Console.printLine("Error: " + e),
//      v => Console.printLine("Value: " + v)
//    )

  val treat3 =
    sandbox1.foldZIO(
      e => Console.printLine("Error: " + e),
      v => Console.printLine("Value: " + v)
    )

  val treat4 =
    sandbox2.foldZIO(
      e => Console.printLine("Error: " + e),
      v => Console.printLine("Value: " + v)
    )

  val treat5 =
    program1.foldCauseZIO(
      e => Console.printLine("Error: " + e),
      v => Console.printLine("Value: " + v)
    )

  val treat6 =
    program2.foldCauseZIO(
      e => Console.printLine("Error: " + e),
      v => Console.printLine("Value: " + v)
    )

  val run = treat6

}
