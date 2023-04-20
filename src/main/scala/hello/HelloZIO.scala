package hello

import zio.*
import java.io.IOException

object HelloZIO extends ZIOAppDefault:

  val msg = "Hello, ZIO"
  val run: ZIO[Any, IOException, Unit] = Console.printLine(msg)
