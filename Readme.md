
# Goose Game

Experimentation in a scala [Goose Game](https://en.wikipedia.org/wiki/Game_of_the_Goose)

## How to run

### Prerequisites

- java sdk (preferably jdk 8)
- scala and sbt accessible from PATH

You can package and run the jar or use sbt

```sh
> sbt package
> scala target/scala-2.13/goose-game_2.13-0.1.0-SNAPSHOT.jar
# Or alternatevely
> sbt run
```

### Playing

Once the game start you are greeted by the game and can start playing

You can add players and move them

```game
( o)> add player Pippo
Players: Pippo
( o)> add player Pluto
Players: Pippo, Pluto
```

you can move players specifying the rolls

```game
( o)> move Pippo 3,5
Pippo rolls 3,5. Pippo moves from Start to 8
```

if omitten the game will launch two d6 for you

```game
( o)> move Pluto
Pluto rolls 4,6. Pluto moves from Start to 10
```

the first player to reach 63 wins. Happy Playing!

## Tests

inside the folder `src/test/scala` there are some tests, you can run them with sbt

``` sh
> sbt test
```

## Caveats

### game

- the add command do not check the second word you can write `add pizza <player>` and it would still works
- the move command respond with a `Invalid  command.  Try  again` if you use a non existing player
- there is no help to help you discover commands.
- using `q` you can quit the game.

### code

- every method of the trait `Game` is public for easyer testing
- error handling is almost not existant, it will print always `Invalid command. Try again`, at least is something.
- the model of the application are string aliases.
