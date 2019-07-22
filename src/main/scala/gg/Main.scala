package gg

import scala.io.StdIn.readLine
// import scala.util.Random.shuffle

object Main extends App with Game {
  intro()
  play()
}

trait Game {
  type Player = String
  type Message = String

  var players: List[Player] = List()
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
      .continually(readLine("( o)> "))
      .foreach {
        case "q" => {
          println("Bye!")
          sys.exit(0)
        }
        case command =>
          run(command) match {
            case None =>
              println("Invalid command. Try again")
            case Some(message) =>
              println(message)

          }
      }

  }

  private def addPlayer(user: String): Option[Player] = {
    this.players.find(_ == user) match {
      case Some(player) => None
      case None => {
        this.players = this.players ::: List(user)
        Some(user)
      }
    }
  }

  private def run(command: String): Option[String] = {
    val arr = command.split("\\s+")
    arr.head match {
      case "add" => {
        val player = arr.tail.tail(0)
        this.addPlayer(player) match {
          case None    => Some(s"$player: already existing player")
          case Some(_) => Some(s"Players: ${this.players.mkString(", ")}")
        }

      }
      case "move" => {

        return Some(command)
      }
      case _ => None
    }
  }

}
