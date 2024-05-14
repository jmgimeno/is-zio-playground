trait Animal:
  def name: String

final case class Cat(name: String) extends Animal

summon[Cat <:< Animal]

// summon[Animal <:< Cat]

// summon[List[Cat] <:< List[Animal]]


// summon[Collection[Cat] <:< Collection[Animal]]

// enum List[+A]:
//    case Nil
//    case Cons(a: A, as: List[A])

summon[List[Cat] <:< List[Animal]]

case class Collection[+A](elements: List[A])

def combine[A](left: Collection[A], right: Collection[A]): Collection[A] =
  Collection(left.elements ::: right.elements)

final case class Dog(name: String) extends Animal

val cats: Collection[Cat] = Collection(List(Cat("spots"), Cat("mittens")))
val dogs: Collection[Dog] = Collection(List(Dog("fido"), Dog("rover")))

val animals: Collection[Animal] = combine(cats, dogs)

def widen[A, B >: A](collection: Collection[A]): Collection[B] =
  Collection(collection.elements)

//val animals = combine(widen[Cat, Animal](cats), widen[Dog, Animal](dogs))

animals


