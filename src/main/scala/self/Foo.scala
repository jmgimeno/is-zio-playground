package self

// Alias for this, specially useful when using Inner classes

class Foo {
  self =>
  val x = 1
  new AnyRef {
    val x = 2
    println(this.x) // 2
    println(self.x) // 1
  }
}

// Self-types are much more complicated https://docs.scala-lang.org/tour/self-types.html
