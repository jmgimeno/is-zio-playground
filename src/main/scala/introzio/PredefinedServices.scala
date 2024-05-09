package introzio

import zio.*

object PredefinedServices extends ZIOAppDefault:

  private val program1: ZIO[Any, Exception, Unit] =
    System.env("JAVA_HOME").flatMap {
      case None        => Console.printLine("Not defined")
      case Some(value) => Console.printLine(s"JAVA_HOME=$value")
    }

  private val program2: ZIO[Any, Nothing, Unit] =
    for
      os <- System.env("JAVA_HOME").orElse(ZIO.none)
      _ <- os match
        case None        => Console.printLine("Not defined").orElse(ZIO.unit)
        case Some(value) => Console.printLine(s"JAVA_HOME=$value").orElse(ZIO.unit)
    yield ()

  val run: ZIO[Any, Exception, Unit] =
    program1
