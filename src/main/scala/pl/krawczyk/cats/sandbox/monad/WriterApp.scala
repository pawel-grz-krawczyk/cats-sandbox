package pl.krawczyk.cats.sandbox.monad

import cats.data.Writer
import cats._
import cats.implicits._

object WriterApp {

  def slowly[A](body: => A) =
    try body
    finally Thread.sleep(100)

  type Logged[A] = Writer[Vector[String], A]

  def factorial(n: Int): Logged[Int] =
    for {
      ans <- slowly(if (n == 0) 1.pure[Logged] else factorial(n - 1).map(n * _))
      _   <- Vector(s"fact $n $ans").tell
    } yield ans

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent._
    import scala.concurrent.duration._

    val _ = Await.result(
      Future.sequence(
        Vector(
          Future(factorial(5)).map(x => println(x.run)),
          Future(factorial(5)).map(x => println(x.run))
        )
      ),
      5.seconds
    )
  }
}
