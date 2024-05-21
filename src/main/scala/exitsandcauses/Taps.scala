package exitsandcauses

import zio.*

object Taps extends ZIOAppDefault {

  val zio1 = ZIO.succeed(42)

  val run1 = zio1.map(_ * 2).debug

  val zio2 = ZIO.fail("patata")

  val run2 = zio2.tap(int => Console.printLine(s"tapped result $int")).debug

  val run3 = zio2.tapError(err => Console.printLine(s"tapped error $err")).debug

  val run = zio2.tapErrorCause(cause => Console.printLine(s"tapped cause $cause")).debug
}
