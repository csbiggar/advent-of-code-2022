package games

import games.Outcome.Draw
import games.Outcome.Lose
import games.Outcome.Win
import games.Shape.Paper
import games.Shape.Rock
import games.Shape.Scissors

fun main() {

    val input = RockPaperScissorsGame::class.java.getResource("/day-2.txt")!!.readText()

    println(League(input).myFinalScore())

}

class League(private val input: String) {
    fun myFinalScore(): Int = input
        .lines()
        .sumOf { gameShapes ->
            val (theirShape, myShape) = gameShapes.split(" ").map { Shape.fromLetter(it) }
            RockPaperScissorsGame.getMyScore(theirShape, myShape)

        }
}

object RockPaperScissorsGame {
    fun getMyScore(theirShape: Shape, myShape: Shape): Int {
        return myShape.score + play(theirShape, myShape).score
    }

    fun play(theirShape: Shape, myShape: Shape): Outcome {
        return when {
            myShape == Rock && theirShape == Scissors -> Win
            myShape == Paper && theirShape == Rock -> Win
            myShape == Scissors && theirShape == Paper -> Win
            myShape == theirShape -> Draw
            else -> Lose
        }

    }
}

enum class Outcome(val score: Int) {
    Win(6),
    Draw(3),
    Lose(0);
}

enum class Shape(val score: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    companion object {
        fun fromLetter(letter: String): Shape = when (letter) {
            "A", "X" -> Rock
            "B", "Y" -> Paper
            "C", "Z" -> Scissors
            else -> throw Exception("Unknown shape! Letter $letter")
        }
    }
}