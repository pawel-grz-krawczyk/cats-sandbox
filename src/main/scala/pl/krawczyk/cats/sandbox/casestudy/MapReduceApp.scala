package pl.krawczyk.cats.sandbox.casestudy

import cats.Monoid
import cats.syntax.semigroup._ // for |+|

object MapReduceApp {

  /** Single-threaded map-reduce function.
   * Maps `func` over `values` and reduces using a `Monoid[B]`.
   */
  def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B = {
    values.foldLeft(Monoid[B].empty)(_ |+| func(_))
  }

  def main(args: Array[String]): Unit = {
  }
}
