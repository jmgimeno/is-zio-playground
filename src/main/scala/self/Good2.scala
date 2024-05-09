package self

object Good2 {

  trait Description {

    self =>

    val name: String = "Outer"
    def desc: String = s"[$name] Description"
    def instance: Description = new Description {
      override val name: String = "Inner"
      override def desc: String = self.desc + s", Name: '${this.name}' with Inner Description"
    }
  }

  @main def mainGood2(): Unit = {
    val bad = new Description() {}
    println(bad.name)
    println(bad.desc)
    val bad2 = bad.instance
    println(bad2.name)
    println(bad2.desc)
  }
}
