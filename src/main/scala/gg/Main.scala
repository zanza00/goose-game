package gg

import scala.io.StdIn.readLine
// import scala.util.Random.shuffle

object Main extends App with Game {
  intro()
  play()
}

trait Game {
  def intro(): Unit = {
    println("""
        __            __
    ___( o)>        <(o )___
    \ <_. )          ( ._> /
     `---'            `---'
    Welcome to Game of Goose.
    """)
  }

  def play(): Unit = {
    Iterator
      .continually(readLine("___( o)> "))
      .foreach {
        case "q" => {
          println("Bye!")
          sys.exit(0)
        }
        case move =>
          run(move) match {
            case None =>
              println("Invalid move. Try again")
            case Some(userMove) =>
              println("you played this move")
              println(userMove)

          }
      }

  }

  private def run(move: String): Option[String] = {

    Some(move)
  }

}
