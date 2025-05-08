package exitsandcauses

import zio.*

object Taps extends ZIOAppDefault {

  val zio1 = ZIO.succeed(42)

  val run1 = zio1.map(_ * 2).debug

  val zio2 = ZIO.fail("patata")

  val run2 = zio1.tap(int => Console.printLine(s"tapped result $int")).debug

  val run3 = zio2.tapError(err => Console.printLine(s"tapped error $err")).debug

  val run4 = zio2.tapErrorCause(cause => Console.printLine(s"tapped cause $cause")).debug

  val run5 = zio2.tapErrorTrace((error, stackTrace) =>
    Console.printLine(s"Error: $error") *>
      Console.printLine(s"Stack Trace: $stackTrace"))

  val run = run2
}
