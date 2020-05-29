package pl.krawczyk.cats.sandbox

import cats._
import cats.implicits._

/*
1.3 Exercise: Printable Library
Scala provides a toString method to let us convert any value to a String.
 However, this method comes with a few disadvantages: it is implemented
  for every type in the language, many implementations are of limited use,
   and we can’t opt-in to specific implementations for specific types.

Let’s define a Printable type class to work around these problems:

Define a type class Printable[A] containing a single method format. format should
 accept a value of type A and return a String.

Create an object PrintableInstances containing instances of Printable for String and Int.

Define an object Printable with two generic interface methods:

format accepts a value of type A and a Printable of the corresponding type.
 It uses the relevant Printable to convert the A to a String.

print accepts the same parameters as format and returns Unit.
 It prints the A value to the console using println.
 */

trait Printable[A] {
  def format(value: A): String
}

object Printable {
  def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)

  def print[A](value: A)(implicit p: Printable[A]): Unit =
    println(p.format(value))
}

object PrintableInstances {
  implicit val stringPrintable: Printable[String] =
    (value: String) => value

  implicit val intPrintable: Printable[Int] =
    (value: Int) => value.toString
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String = p.format(value)
    def print(implicit p: Printable[A]): Unit    = println(p.format(value))
  }
}

object Test {

  case class Cat(name: String, age: Int, color: String)
  object Cat {
    implicit val catShow: Show[Cat] = Show.show { (cat: Cat) =>
      val name  = cat.name.show
      val age   = cat.age.show
      val color = cat.color.show
      s"$name is a $age year-old $color cat."
    }

    implicit val catEq: Eq[Cat] = Eq.instance { (c1, c2) =>
      c1.color === c2.color && c1.name === c2.name && c1.age === c2.age
    }
  }

  def main(args: Array[String]): Unit = {
    val cat = Cat("Tom", 7, "grey")

    println(cat.show)

    val cat1 = Cat("Garfield", 38, "orange and black")
    val cat2 = Cat("Heathcliff", 33, "orange and black")

    val optionCat1 = Option(cat1)
    val optionCat2 = Option.empty[Cat]
    println(cat1 === cat2)
    println(optionCat1 === optionCat2)
  }
}
