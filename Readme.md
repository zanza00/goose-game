
# Goose Game

Experimentation in a scala [Goose Game](https://en.wikipedia.org/wiki/Game_of_the_Goose)

## How to run 
### Prerequisites
- java sdk (preferably jdk 8)
- scala and sbt accessible from PATH

You can package and run the jar or use sbt 
```
> sbt package
> scala target/scala2.13/goose-game.jar
# Or alternatevely
> sbt run
```
Once the game start you are greeted by the game and can start playing
You can add players and move them
```
> add player Pippo
> add player Pluto
```
```
move Pippo 3,5
move Pluto
```

## Tests
inside the folder `src/test/scala` there are the tests, you can run them with sbt
```
> sbt test
```

## Caveats
### game
- the add command do not check the second word you can write `add pizza <player>` and it would still works
- the move command respond with a `Invalid  command.  Try  again` if you use a non existing player
- there is no help to help you discover commands
- using `q` you can quit the game
### code
- every method of the trait `Game` is public for easyer testing
- error handling is almost not existant, it will print always `Invalid command. Try again`, at least is something.
- the model of the application are string aliases

