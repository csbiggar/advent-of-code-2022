package games

import games.Outcome.Draw
import games.Outcome.Lose
import games.Outcome.Win
import games.RockPaperScissorsGame.getMyShape
import games.RockPaperScissorsGame.play
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
        .sumOf { game ->
            val theirShape = Shape.fromLetter(game.substringBefore(" "))
            val outcome = Outcome.fromLetter(game.substringAfter(" "))
            RockPaperScissorsGame.getMyScore(getMyShape(theirShape, outcome), outcome)
        }
}

object RockPaperScissorsGame {
    fun getMyScore(myShape: Shape, outcome: Outcome): Int {
        return myShape.score + outcome.score
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

    fun getMyShape(theirShape: Shape, outcome: Outcome): Shape {
        return when {
            outcome == Win && theirShape == Scissors -> Rock
            outcome == Win && theirShape == Rock -> Paper
            outcome == Win && theirShape == Paper -> Scissors
            outcome == Lose && theirShape == Scissors -> Paper
            outcome == Lose && theirShape == Rock -> Scissors
            outcome == Lose && theirShape == Paper -> Rock
            else -> theirShape
        }
    }

}

enum class Outcome(val score: Int) {
    Win(6),
    Draw(3),
    Lose(0);

    companion object {
        fun fromLetter(letter: String): Outcome = when (letter) {
            "X" -> Lose
            "Y" -> Draw
            "Z" -> Win
            else -> throw Exception("Unknown outcome! Letter $letter")
        }
    }
}

enum class Shape(val score: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    companion object {
        fun fromLetter(letter: String): Shape = when (letter) {
            "A" -> Rock
            "B" -> Paper
            "C" -> Scissors
            else -> throw Exception("Unknown shape! Letter $letter")
        }
    }
}