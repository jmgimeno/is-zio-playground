package variance

object Contravariance:

  trait Fruit
  case class Apple() extends Fruit
  case class Orange() extends Fruit

  trait Drink

  trait FoodProcessor[-Ingredient]:
    def process(ingredient: Ingredient): Drink
    def processAndResidue[Ingredient1 <: Ingredient](ingredient: Ingredient1): (Drink, Ingredient1)

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
    val (drink, appleRes) =
      processor.processAndResidue(new Apple)
//    val (drink2, orangeRes) =
//      processor.processAndResidue(new Orange)

  g(fruitProcessor)

  val appleProcessor: FoodProcessor[Apple] = ???
  g(appleProcessor)

  val orangeProcessor: FoodProcessor[Orange] = ???
  // g(orangeProcessor)


  object MySpecificProcessor extends FoodProcessor[Apple]:
    def process(i: Apple): Drink = ???
    def processAndResidue[A1 <: Apple](ingredient: A1): (Drink, A1) = ???

  def h() =
    MySpecificProcessor.processAndResidue(new Apple)

  def gg(processor: FoodProcessor[Fruit]) =
    val (drink, appleRes) =
      processor.processAndResidue(new Apple)
    val (drink2, orangeRes) =
      processor.processAndResidue(new Orange)

  // gg(appleProcessor)
  // gg(orangeProcessor)
