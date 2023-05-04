package variance

import zio.*

object Covariance:

  trait Gen[-R, +A]:
    def sample: ZIO[R, Nothing, A]
    // def contains(a: A): ZIO[R, Nothing, Boolean]

object Contravariant:

  trait Drink

  // trait FoodProcessor[-Ingredient]:
  //   def process(ingredient: Ingredient): (Drink, Ingredient)
