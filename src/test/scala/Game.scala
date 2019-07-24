package gg

import org.scalatest._

class GameSpec extends FlatSpec with BeforeAndAfterEach with Game {

  override def beforeEach() {
    Game.cleanup()
  }

  "A game" should "can add a player" in {
    Game.addPlayer("Pippo")
    assert(Game.positions.get("Pippo") == Some(0))
    println(Game.positions)
  }

  "A game" should "can add two player" in {
    Game.addPlayer("Pluto")
    Game.addPlayer("Paperino")
    assert(Game.positions.get("Pluto") == Some(0))
    assert(Game.positions.get("Paperino") == Some(0))
    assert(Game.positions.get("Pippo") == None)
  }

  "A move" should "be able to move a player" in {
    Game.addPlayer("Pippo")
    val res = Game.handleResult(Some(4), 0, "Pippo", ("2, 2", 4))
    assert(
      res === Some(
        "Pippo rolls 2, 2. Pippo moves from Start to 4"
      )
    )
  }

  "A move" should "be able to move the player again if hits the brigde" in {
    Game.addPlayer("Pippo")
    val res = Game.handleResult(Some(6), 4, "Pippo", ("1, 1", 2))
    assert(
      res === Some(
        "Pippo rolls 1, 1. Pippo moves from 4 to The Bridge. Pippo jumps to 12"
      )
    )
  }

  "A move" should "be able to move the player again if hits the Goose" in {
    Game.addPlayer("Pippo")
    val res = Game.handleResult(Some(5), 3, "Pippo", ("1, 1", 2))
    assert(
      res === Some(
        "Pippo rolls 1, 1. Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7"
      )
    )
  }
  "A move" should "be able to move the player again if hits the Goose two times" in {
    Game.addPlayer("Pippo")
    val res = Game.handleResult(Some(14), 10, "Pippo", ("2, 2", 4))
    assert(
      res === Some(
        "Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22"
      )
    )
  }

  "the goose" should "be able to handle a 4 jump" in {
    val res = Game.calculateJumps(0, 9, 1)
    assert(res == (36, 4))
  }

  "a player" should "be able to win" in {
    Game.addPlayer("Pippo")
    val res = Game.handleResult(Some(63), 60, "Pippo", ("1, 2", 3))
    assert(
      res === Some(
        "Pippo rolls 1, 2. Pippo moves from 60 to 63. Pippo Wins!!"
      )
    )
  }
  "a player" should "be bounce back if overshoots" in {
    Game.addPlayer("Pippo")
    val res = Game.handleResult(Some(65), 60, "Pippo", ("3, 2", 5))
    assert(
      res === Some(
        "Pippo rolls 3, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61"
      )
    )
  }
}
