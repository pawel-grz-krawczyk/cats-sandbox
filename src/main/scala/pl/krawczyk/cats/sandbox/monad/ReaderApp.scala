package pl.krawczyk.cats.sandbox.monad

import cats._
import cats.data.Reader
import cats.implicits._

object ReaderApp {
  final case class Db(usernames: Map[Int, String], passwords: Map[String, String])

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords.get(username).contains(password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      username <- findUsername(userId)
      passwordOK <- username
        .map(checkPassword(_, password))
        .getOrElse(false.pure[DbReader])
    } yield passwordOK

  def main(args: Array[String]): Unit = {
    val users = Map(
      1 -> "dade",
      2 -> "kate"
    )
    val passwords = Map(
      "dade" -> "zerocool",
      "kate" -> "acidburn"
    )
    val db = Db(users, passwords)

    println(checkLogin(1, "zerocool").run(db))
    println(checkLogin(2, "wrongPass").run(db))
    println(checkLogin(4, "davinci").run(db))
  }
}
