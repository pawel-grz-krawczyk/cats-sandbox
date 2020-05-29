package pl.krawczyk.cats.sandbox

import cats._
import cats.implicits._

object FunctorApp {
  sealed trait Tree[+A]
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A)                        extends Tree[A]

  object Tree {
    implicit def treeFunctor: Functor[Tree] = new Functor[Tree] {
      override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] =
        fa match {
          case Leaf(value)         => Leaf(f(value))
          case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        }
    }

    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
      Branch(left, right)

    def leaf[A](value: A): Tree[A] =
      Leaf(value)
  }

  def main(args: Array[String]): Unit = {
    val tree: Tree[Int] = Branch(
      Branch(Leaf(1), Leaf(2)),
      Branch(Leaf(3), Leaf(4))
    )
    val mappedTree = Functor[Tree].map(tree)(x => x + 1)
    println(mappedTree)
    println(Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2))
    println(tree.map(_ * 2))
  }
}
