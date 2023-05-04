package introzio

import zio.*

object PredefinedServices extends ZIOAppDefault:

  val run =
    System.env("JAVA_HOME").flatMap {
      case None        => Console.printLine("Not defined")
      case Some(value) => Console.printLine(s"JAVA_HOME=$value")
    }
