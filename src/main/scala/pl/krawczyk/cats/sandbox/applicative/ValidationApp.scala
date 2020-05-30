package pl.krawczyk.cats.sandbox.applicative

import cats.data.Validated
import cats.implicits._

object ValidationApp {

  case class User(name: String, age: Int)

  type FormData    = Map[String, String]
  type FailFast[A] = Either[List[String], A]
  type FailSlow[A] = Validated[List[String], A]

  private def getValue(name: String)(data: FormData): FailFast[String] =
    data.get(name).toRight(List(s"$name field not specified"))

  def parseInt(name: String)(data: String): FailFast[Int] =
    Either.catchOnly[NumberFormatException](data.toInt).leftMap(_ => List(s"$name must be an integer"))

  def nonBlank(name: String)(data: String): FailFast[String] =
    Right(data).ensure(List(s"$name must be non-empty"))(_.nonEmpty)

  def nonNegative(name: String)(data: Int): FailFast[Int] =
    Right(data).ensure(List(s"$name must be non-negative"))(_ >= 0)

  def readName(data: FormData): FailFast[String] = {
    val field = "name"
    getValue(field)(data)
      .flatMap(nonBlank(field))
  }

  def readAge(data: FormData): FailFast[Int] = {
    val field = "age"
    getValue(field)(data)
      .flatMap(parseInt(field))
      .flatMap(nonNegative(field))
  }

  def readUser(data: FormData): FailSlow[User] =
    (
      readName(data).toValidated,
      readAge(data).toValidated
    ).mapN(User.apply)

  def main(args: Array[String]): Unit = {
    println(readUser(Map("name" -> "Dave", "age" -> "37")))
    println(readUser(Map("age"  -> "-1")))
  }
}
