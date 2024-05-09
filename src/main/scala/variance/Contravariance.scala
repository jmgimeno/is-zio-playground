package variance

import zio.*

object Contravariance:

  trait Drink

  // trait FoodProcessor[-Ingredient]:
  //   def process(ingredient: Ingredient): (Drink, Ingredient)
