package pl.krawczyk.cats.sandbox

import cats.instances.string._
import cats.syntax.semigroup._

object Main extends App {
  println("Hello " |+| "Cats!")
}
