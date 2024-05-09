package self

object Bad {

  trait Description {
    val name: String = "Outer"

    def desc: String = s"[$name] Description"

    def instance: Description = new Description {
      override val name: String = "Inner"

      override def desc: String = this.desc + s", Name: '${this.name}' with Inner Description"
    }
  }

  @main def mainBad(): Unit = {
    val description = new Description() {}
    println(description.name)
    println(description.desc)
    val bad2 = description.instance
    println(bad2.name)
    println(bad2.desc) // Infinite loop
  }
}
