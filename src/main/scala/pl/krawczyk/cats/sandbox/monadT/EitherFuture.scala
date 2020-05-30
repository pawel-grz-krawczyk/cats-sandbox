package pl.krawczyk.cats.sandbox.monadT

import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.concurrent.duration._

object EitherFuture {
  type Error       = String
  type Response[A] = EitherT[Future, Error, A]

  val powerLevels = Map(
    "Jazz"      -> 6,
    "Bumblebee" -> 8,
    "Hot Rod"   -> 10
  )

  def getPowerLevel(autobot: String): Response[Int] =
    powerLevels.get(autobot) match {
      case Some(lvl) => lvl.pure[Response] // or EitherT.right(Future(lvl))
      case None      => EitherT.left(s"Not reachable $autobot".pure[Future])
    }

  private val SPECIAL_MOVE_THRESHOLD = 15

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
    for {
      p1 <- getPowerLevel(ally1)
      p2 <- getPowerLevel(ally2)
    } yield {
      (p1 + p2 > SPECIAL_MOVE_THRESHOLD)
    }

  def tacticalReport(ally1: String, ally2: String): String = {
    val canSpecialMoveFuture = canSpecialMove(ally1, ally2).value

    Await.result(canSpecialMoveFuture, 1.second) match {
      case Left(msg) =>
        s"Comms error: $msg"
      case Right(true) =>
        s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) =>
        s"$ally1 and $ally2 need a recharge."
    }
  }

  def main(args: Array[String]): Unit = {
    println(tacticalReport("Jazz", "Bumblebee"))
    println(tacticalReport("Bumblebee", "Hot Rod"))
    println(tacticalReport("Jazz", "Ironhide"))
  }
}
