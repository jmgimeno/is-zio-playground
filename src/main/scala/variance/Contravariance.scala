package variance

import zio.*

object Contravariance:

  trait Fruit
  case class Apple() extends Fruit
  case class Orange() extends Fruit

  trait Drink

  trait FoodProcessor[-Ingredient]:
    def process(ingredient: Ingredient): Drink
    def processAndResidue[Residue <: Ingredient](ingredient: Ingredient): (Drink, Residue)

  summon[Apple <:< Fruit]

  //summon[FoodProcessor[Apple] <:< FoodProcessor[Fruit]]
  //summon[FoodProcessor[Fruit] <:< FoodProcessor[Apple]]

  // FoodProcessor[Apple] -> pot processar qualsevol poma, però no podrà processar taronges
  // FoodProcessor[Fruit] -> pot processar tant pomes com taronges

  def f(processor: FoodProcessor[Apple]) =
    //processor.process(new Orange)
    processor.process(new Apple)

  val fruitProcessor: FoodProcessor[Fruit] = ???

  f(fruitProcessor)

  // Apple <:< Fruit => FoodProcessor[Apple] >:> FoodProcessor[Fruit]
  //                 => FoodProcessor[Fruit] <:< FoodProcessor[Apple]

  def g(processor: FoodProcessor[Apple]) =
    val (drink, appleRes): (Drink, Apple) =
      processor.processAndResidue(new Apple)

  g(fruitProcessor)
