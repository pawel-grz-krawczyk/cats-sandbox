package pl.krawczyk.cats.sandbox

import cats._
import cats.implicits._

case class Order(totalCost: Double, quantity: Double)
object Order {
  implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order =
      Order(totalCost = x.totalCost + y.totalCost, quantity = x.quantity + y.quantity)
  }
}

object App {

  import Order._

  def add[A: Monoid](items: List[A]): A =
    items.foldLeft(Monoid[A].empty)(_ |+| _)

  def main(args: Array[String]): Unit = {
    println(add(List(1, 2, 3)))
    println(add(List(1.some, 2.some, 3.some, None)))
    println(
      add(
        List(
          Order(10, 1),
          Order(20, 2)
        )
      )
    )
  }
}
