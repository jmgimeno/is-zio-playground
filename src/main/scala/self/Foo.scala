package self

// Alias for this, specially useful when using Inner classes

class Foo:
  self =>
  class Bar:
    def getFoo: Foo = self

// Self-types are much more complicated https://docs.scala-lang.org/tour/self-types.html
