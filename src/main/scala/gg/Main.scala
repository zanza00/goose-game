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
  import scala.collection.mutable.Map

  var players: List[Player] = List()
  val positions: Map[Player, Int] = Map[Player, Int]()
  val rng = scala.util.Random

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
        this.positions.put(user, 0)
        Some(user)
      }
    }
  }

  private def getCurrentPosition(player: Player): Option[Int] =
    this.positions.get(player)

  private def getNewPosition(player: Player, amount: Int): Option[Int] =
    this.getCurrentPosition(player).map(_ + amount)

  private def updatePlayerPosition(player: Player, position: Int): Unit =
    this.positions.update(player, position)

  private def throwRandomDice(): (String, Int) = {
    val rolls = Array(this.rng.nextInt(6) + 1, this.rng.nextInt(6) + 1)
    (rolls.mkString(","), rolls.sum)
  }

  private def calculateJumps(start: Int, move: Int, jumps: Int): (Int, Int) = {
    val newPosition = start + move
    newPosition match {
      case 5 | 9 | 14 | 18 | 23 | 27 => {
        this.calculateJumps(newPosition, move, jumps + 1)
      }
      case _ => (newPosition, jumps)
    }
  }

  private def run(command: String): Option[String] = {
    val arr = command.split("\\s+")
    arr.head match {
      case "add" => {
        val player = arr.tail.tail.head
        this.addPlayer(player) match {
          case None    => Some(s"$player: already existing player")
          case Some(_) => Some(s"Players: ${this.players.mkString(", ")}")
        }

      }
      case "move" => {
        val player = arr.tail.head
        val move = arr.tail.tail.headOption match {
          case None => this.throwRandomDice()
          case Some(amount) => {
            val sum = amount.split(",").map(_.toInt).sum
            (amount, sum)
          }
        }

        val currentPosition = this.getCurrentPosition(player).getOrElse(0)
        val newPosition = this.getNewPosition(player, move._2)

        newPosition match {
          case None =>
            Some(
              s"Invalid player, avaliable players are ${this.players.mkString(", ")}"
            )
          case Some(position) =>
            position match {
              case 63 => {
                this.updatePlayerPosition(player, position)
                Some(
                  s"$player rolls ${move._1}. $player moves from $currentPosition to 63. $player Wins!!"
                )
              }
              case x if x > 63 => {
                val bounceAmount = position - 63
                val bouncedPosition = 63 - bounceAmount

                this.updatePlayerPosition(player, bouncedPosition)
                Some(
                  s"$player rolls ${move._1}. $player moves from $currentPosition to 63. $player bounces! Pippo returns to $bouncedPosition"
                )
              }

              case 6 => {
                this.updatePlayerPosition(player, 12)
                Some(
                  s"$player rolls ${move._1}. $player moves from $currentPosition to The Bridge. Pippo jumps to 12"
                )
              }
              case 5 | 9 | 14 | 18 | 23 | 27 => {
                val jumpsData = this.calculateJumps(position, move._2, 1)
                val positionAfterJumps = jumpsData._1
                val numberOfJumps = jumpsData._2
                this.updatePlayerPosition(player, positionAfterJumps)
                if (numberOfJumps == 1) {
                  Some(
                    s"$player rolls ${move._1}. $player moves from $currentPosition to $position, The Goose. Pippo moves again and goes to $positionAfterJumps"
                  )
                } else {
                  Some(
                    s"$player rolls ${move._1}. $player moves from $currentPosition to $position, The Goose. Pippo moves again and goes to ${positionAfterJumps - move._2}, The Goose. Pippo moves again and goes to $positionAfterJumps"
                  )
                }

              }
              case _ => {
                this.updatePlayerPosition(player, position)
                Some(
                  s"$player rolls ${move._1}. $player moves from $currentPosition to $position."
                )
              }
            }
        }

      }
      case _ => None
    }
  }

}
