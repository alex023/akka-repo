
object Study {

  trait Animal

  case class Dog(name: String, age: Int) extends Animal

  case class Cat(name: String) extends Animal

  case class Pet(pet: Animal)

  def main(args: Array[String]): Unit = {
    val mine = Dog("Wolf", 3)

    def equal(msg: Any): Unit = msg match {
      case Pet(`mine`) =>
        println(s"$mine")
      case _ => println("not realize")
    }

    equal(Pet(mine))
    equal(Pet(Dog("Wolf", 3)))
    equal(Pet(Cat("kitty")))
  }

}
