trait Animal:
  def name: String

final case class Cat(name: String) extends Animal

trait Collection[A]

summon[Cat <:< Animal]

// summon[Animal <:< Cat]

summon[List[Cat] <:< List[Animal]]

//summon[Collection[Cat] <:< Collection[Animal]]
