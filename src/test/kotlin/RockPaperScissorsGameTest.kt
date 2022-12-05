import games.Outcome
import games.Outcome.*
import games.RockPaperScissorsGame
import games.Shape
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RockPaperScissorsGameTest {
    @ParameterizedTest(name = "Letter {0} is shape {1}")
    @CsvSource(
        "A,Rock",
        "X,Rock",
        "B,Paper",
        "Y,Paper",
        "C,Scissors",
        "Z,Scissors",
    )
    fun `should return shape for letter`(letter: String, shape: Shape) {
        assertThat(Shape.fromLetter(letter)).isEqualTo(shape)
    }

    @ParameterizedTest(name = "Winning combination my shape {1} over their shape {0}")
    @CsvSource(
        "Scissors,Rock",
        "Rock,Paper",
        "Paper,Scissors",
    )
    fun `should return win result`(theirShape: Shape, myShape: Shape) {
        assertThat(RockPaperScissorsGame.play(theirShape, myShape)).isEqualTo(Win)
    }

    @ParameterizedTest(name = "Draw with {0}")
    @CsvSource(
        "Scissors",
        "Rock",
        "Paper",
    )
    fun `should return draw when shapes are the same`(shape: Shape) {
        assertThat(RockPaperScissorsGame.play(shape, shape)).isEqualTo(Draw)
    }

    @ParameterizedTest(name = "Losing combination their shape {0} over my shape {1}")
    @CsvSource(
        "Rock,Scissors",
        "Paper,Rock",
        "Scissors,Paper",
    )
    fun `should lose with any other combination`(theirShape: Shape, myShape: Shape) {
        assertThat(RockPaperScissorsGame.play(theirShape, myShape)).isEqualTo(Lose)
    }

    @ParameterizedTest(name = "{2} with {1} gives score {3}")
    @CsvSource(
        "Rock,Scissors,Lose,3",
        "Paper,Scissors,Win,9",
    )
    fun `should score`(theirShape: Shape, myShape: Shape, outcome: Outcome, score: Int) {
        assertThat(RockPaperScissorsGame.play(theirShape, myShape)).isEqualTo(outcome)
        assertThat(RockPaperScissorsGame.getMyScore(theirShape, myShape)).isEqualTo(score)
    }

}

