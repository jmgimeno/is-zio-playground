import zio.*

object Presentations extends ZIOAppDefault:

  case class Presentation(name: String, system: String):
    override def toString = s"$name - $system"

  val presentations = List(
    Presentation("Josep Barberà Reverté", "Gradle"),
    Presentation("Daniel Bistuer Marco", "SQLAlchemy"),
    Presentation("Bernat Cases Roca", "Lit.dev"),
    Presentation("Zihan Chen", "Node.js"),
    Presentation("Quim Farrés Masana", "Mypy"),
    Presentation("Sergi Fernández Espona", "JBehave"),
    Presentation("Gerard Monsó Salmons", "Selenium WebDriver"),
    Presentation("Theo Moreno Lomero", "Metasploit"),
    Presentation("Alex Mozota Mejon", "Flask"),
    Presentation("Andreu Perez Torra", "Eclipse Collections")
  )

  val run =
    Random.shuffle(presentations).flatMap { shuffled =>
      ZIO.foreach(shuffled.zipWithIndex) { (presentation, index) =>
        Console.printLine(f"${index + 1}%2d: $presentation%s")
          *> Console
          .printLine("-" * 50)
          .when(index == presentations.length / 2 - 1)
      }
    }
