package pl.krawczyk.cats.sandbox.traverse

import cats.Monoid

object TraverseApp {

  def map[A, B](list: List[A])(func: A => B): List[B] =
    list.foldRight(List.empty[B]) { (item, accum) =>
      func(item) :: accum
    }

  map(List(1, 2, 3))(_ * 2)
  // res9: List[Int] = List(2, 4, 6)

  def flatMap[A, B](list: List[A])(func: A => List[B]): List[B] =
    list.foldRight(List.empty[B]) { (item, accum) =>
      func(item) ::: accum
    }

  flatMap(List(1, 2, 3))(a => List(a, a * 10, a * 100))
  // res10: List[Int] = List(1, 10, 100, 2, 20, 200, 3, 30, 300)

  def filter[A](list: List[A])(func: A => Boolean): List[A] =
    list.foldRight(List.empty[A]) { (item, accum) =>
      if(func(item)) item :: accum else accum
    }

  filter(List(1, 2, 3))(_ % 2 == 1)
  // res11: List[Int] = List(1, 3)


  import scala.math.Numeric

  def sumWithNumeric[A](list: List[A])
                       (implicit numeric: Numeric[A]): A =
    list.foldRight(numeric.zero)(numeric.plus)

  sumWithNumeric(List(1, 2, 3))
  // res12: Int = 6

  def sumWithMonoid[A](list: List[A])
                      (implicit monoid: Monoid[A]): A =
    list.foldRight(monoid.empty)(monoid.combine)

  import cats.instances.int._ // for Monoid

  sumWithMonoid(List(1, 2, 3))
  // res13: Int = 6
}
