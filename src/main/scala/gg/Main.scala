package gg

import scala.io.StdIn.readLine
// import scala.util.Random.shuffle

object Main extends App with Game {
  intro()
  play()
}

trait Game {
  def intro(): Unit = {
    println("Welcome to Game of Goose.")
  }

  def play(): Unit = {
    val move = readLine("your move> ")
    if (move == "q") {
      println("Bye!")
      sys.exit(0)
    }
    run(move) match {
      case None =>
        println("Invalid move. Try again")
        play()
      case Some(userMove) =>
        println("you played this move")
        println(userMove)

    }
  }

  private def run(move: String): Option[String] = {

    Some(move)
  }

}
