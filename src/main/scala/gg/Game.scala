package gg

import scala.io.StdIn.readLine

trait Game {
  type Player = String
  type Message = String
  import scala.collection.mutable.Map

  val positions: Map[Player, Int] = Map[Player, Int]()
  val rng = scala.util.Random

  def cleanup() {
    positions.clear()
  }

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
    intro()
    Iterator
      .continually(readLine("( o)> "))
      .takeWhile(_ != "q")
      .foreach {
        case command =>
          run(command) match {
            case None =>
              println("Invalid command. Try again")
            case Some(message) =>
              println(message)

          }
      }

  }

  def addPlayer(user: String): Option[Player] = {
    positions.keySet.find(_ == user) match {
      case Some(player) => None
      case None => {
        positions.put(user, 0)
        Some(user)
      }
    }
  }

  def getCurrentPosition(player: Player): Option[Int] =
    positions.get(player)

  def getNewPosition(player: Player, amount: Int): Option[Int] =
    getCurrentPosition(player).map(_ + amount)

  def updatePlayerPosition(player: Player, position: Int): Unit =
    positions.update(player, position)

  def throwRandomDice(): (String, Int) = {
    val rolls = Array(rng.nextInt(6) + 1, rng.nextInt(6) + 1)
    (rolls.mkString(","), rolls.sum)
  }

  def calculateJumps(start: Int, move: Int, jumps: Int): (Int, Int) = {
    val newPosition = start + move
    newPosition match {
      case 5 | 9 | 14 | 18 | 23 | 27 => {
        calculateJumps(newPosition, move, jumps + 1)
      }
      case _ => (newPosition, jumps)
    }
  }

  def handleResult(
      newPosition: Option[Int],
      currentPosition: Int,
      player: String,
      move: (String, Int)
  ): Option[String] = {
    val currentPositionStartOrLetters = currentPosition match {
      case 0 => "Start"
      case x => x.toString
    }
    newPosition match {
      case None =>
        Some(
          s"Invalid player, avaliable players are ${positions.keySet.mkString(", ")}"
        )
      case Some(position) =>
        position match {
          case 63 => {
            updatePlayerPosition(player, position)
            Some(
              s"$player rolls ${move._1}. $player moves from $currentPositionStartOrLetters to 63. $player Wins!!"
            )
          }
          case x if x > 63 => {
            val bounceAmount = position - 63
            val bouncedPosition = 63 - bounceAmount

            updatePlayerPosition(player, bouncedPosition)
            Some(
              s"$player rolls ${move._1}. $player moves from $currentPositionStartOrLetters to 63. $player bounces! Pippo returns to $bouncedPosition"
            )
          }

          case 6 => {
            updatePlayerPosition(player, 12)
            Some(
              s"$player rolls ${move._1}. $player moves from $currentPositionStartOrLetters to The Bridge. $player jumps to 12"
            )
          }
          case 5 | 9 | 14 | 18 | 23 | 27 => {
            val jumpsData = calculateJumps(position, move._2, 1)
            val positionAfterJumps = jumpsData._1
            val numberOfJumps = jumpsData._2
            updatePlayerPosition(player, positionAfterJumps)
            val jumpPhrases = List
              .range(1, numberOfJumps + 1)
              .map(n => {
                if (n == 1) {
                  s"$player moves from $currentPositionStartOrLetters to ${currentPosition + move._2}, The Goose."
                } else {
                  s"$player moves again and goes to ${currentPosition + move._2 * n}, The Goose."
                }
              })
            Some(
              s"$player rolls ${move._1}. ${jumpPhrases.mkString(" ")} $player moves again and goes to $positionAfterJumps"
            )

          }
          case _ => {
            updatePlayerPosition(player, position)
            Some(
              s"$player rolls ${move._1}. $player moves from $currentPositionStartOrLetters to $position"
            )
          }
        }
    }

  }

  def run(command: String): Option[String] = {
    val arr = command.split("\\s+")
    arr.head match {
      case "add" => {
        val player = arr.tail.tail.head
        addPlayer(player) match {
          case None    => Some(s"$player: already existing player")
          case Some(_) => Some(s"Players: ${positions.keySet.mkString(", ")}")
        }

      }
      case "move" => {
        val player = arr.tail.head
        val move = arr.tail.tail.headOption match {
          case None => throwRandomDice()
          case Some(amount) => {
            val sum = amount.split(",").map(_.toInt).sum
            (amount, sum)
          }
        }

        val currentPosition = getCurrentPosition(player).getOrElse(0)
        val newPosition = getNewPosition(player, move._2)
        handleResult(newPosition, currentPosition, player, move)

      }
      case _ => None
    }
  }
}

object Game extends Game
