package pl.krawczyk.cats.sandbox.casestudy

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.int._
import scala.concurrent.duration._
import cats.Monoid
import cats.syntax.semigroup._ // for |+|
import cats.syntax.foldable._ // for combineAll and foldMap

//import cats._
//import cats.implicits._

import cats.instances.future._ // for Applicative
import cats.instances.vector._ // for Traverse
import cats.syntax.traverse._ // for sequence

object MapReduceApp {

  private val AVAILABLE_PROCESSORS = Runtime.getRuntime.availableProcessors

  /** Single-threaded map-reduce function.
    * Maps `func` over `values` and reduces using a `Monoid[B]`.
    */
  def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B =
    values.foldLeft(Monoid[B].empty)(_ |+| func(_))

  def parallelFoldMap[A, B: Monoid](values: Vector[A])(func: A => B): Future[B] = {
    val groupSize = (1.0 * values.size / AVAILABLE_PROCESSORS).ceil.toInt
    values
      .grouped(groupSize)
      .toVector
      .traverse(v => Future(foldMap(v)(func)))
      .map(_.combineAll)
  }

  def main(args: Array[String]): Unit = {
    val result: Future[Int] =
      parallelFoldMap((1 to 1000000).toVector)(identity)
    println(Await.result(result, 1.second))
  }
}
