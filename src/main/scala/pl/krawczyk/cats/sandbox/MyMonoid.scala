package pl.krawczyk.cats.sandbox

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]) =
    monoid
}

object MonoidInstances {
  implicit val booleanAndMonoid: Monoid[Boolean] = {
    new Monoid[Boolean] {
      override def empty: Boolean = true

      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }
  }

  implicit val booleanOrMonoid: Monoid[Boolean] = {
    new Monoid[Boolean] {
      override def empty: Boolean = false

      override def combine(x: Boolean, y: Boolean): Boolean = x || y
    }
  }

  implicit val intPrintable: Printable[Int] =
    (value: Int) => value.toString
}
