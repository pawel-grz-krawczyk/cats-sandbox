package pl.krawczyk.cats.sandbox

import cats._
import cats.implicits._

object ShowExample {

  def main(args: Array[String]): Unit = {
    val showInt: Show[Int]       = Show.apply[Int]
    val showString: Show[String] = Show.apply[String]

    println(showInt.show(42))
    println(showString.show("foo"))

    println(42.show)
    println("foo".show)

//    implicit val dateShow: Show[Date] =
//      Show.show(date => s"${date.getTime}ms since the epoch.")
  }
}
