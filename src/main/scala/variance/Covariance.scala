package variance

import zio.*

object Covariance:

  trait Gen[R, +A]:
    def sample: ZIO[R, Nothing, A]
    def contains[A1 >: A](a: A1): ZIO[R, Nothing, Boolean]

/*
  Gen[R, Cat] -> Gen que pot generar Cats
  Gen[R, Animal] -> Gen que pot generar Animals

  def f(gen: Gen[Any, Animal]) =
    gen.contains(new Dog("dharma"))

  Gen[R, Cat] és subclasse de Gen[R, Animal] ; ja que Gen es covariant respecte de A

  val genCat: Gen[Any, Cat]

  Abans de redefinir contains

    def contains(a: A): ZIO[R, Nothing, Boolean]

  f(genCat) ; ho hauria de poder fer
    -> genCat.contains(new Dog("dharma"))
       ^^^^^^          ^^^^^^^^^^^^^^^^
       Gen[Any, Cat]      NO ÉS UN Cat !!!!

  ------------------------------------------------------

  Després de redefinir contains

  def contains[A1 >: A](a: A1): ZIO[R, Nothing, Boolean]


    genCat.contains(new Dog("dharma"))
    ^^^^^^          ^^^^^^^^^^^^^^^^^^
    Gen[Any, Cat]    NO ÉS UN Cat
                     PERÒ ÉS UN Animal !!!

 */
