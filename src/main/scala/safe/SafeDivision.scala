package safe

import zio.*

object SafeDivision extends ZIOAppDefault {

  def safeDivision(x: Int, y: Int): ZIO[Any, Unit, Int] =
    ZIO
      .attempt(x / y)
      .foldZIO(
        _ => ZIO.fail(()),
        d => ZIO.succeed(d)
      )

  val run =
    safeDivision(9, 3).foldZIO(
      e => Console.printLine("error"),
      d => Console.printLine(s"result $d")
    )
}
