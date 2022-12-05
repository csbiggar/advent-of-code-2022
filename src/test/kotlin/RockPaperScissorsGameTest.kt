import games.Outcome
import games.Outcome.*
import games.RockPaperScissorsGame
import games.Shape
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RockPaperScissorsGameTest {
    @ParameterizedTest(name = "Letter {0} is shape {1}")
    @CsvSource(
        "A,Rock",
        "B,Paper",
        "C,Scissors",
    )
    fun `should return shape for letter`(letter: String, shape: Shape) {
        assertThat(Shape.fromLetter(letter)).isEqualTo(shape)
    }

    @ParameterizedTest(name = "Letter {0} is outcome {1}")
    @CsvSource(
        "X,Lose",
        "Y,Draw",
        "Z,Win",
    )
    fun `should return outcome for letter`(letter: String, outcome: Outcome) {
        assertThat(Outcome.fromLetter(letter)).isEqualTo(outcome)
    }

    @Nested
    inner class FindResult {

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
    }

    @ParameterizedTest(name = "{2} with {1} gives score {3}")
    @CsvSource(
        "Scissors,Lose,3",
        "Scissors,Win,9",
    )
    fun `should score`(myShape: Shape, outcome: Outcome, score: Int) {
        assertThat(RockPaperScissorsGame.getMyScore(myShape, outcome)).isEqualTo(score)
    }

    @Nested
    inner class FindMyShape {

        @ParameterizedTest(name = "Draw: my shape is {0}, same as theirs")
        @CsvSource(
            "Scissors",
            "Rock",
            "Paper",
        )
        fun `should return same shape with a draw`(shape: Shape) {
            assertThat(RockPaperScissorsGame.getMyShape(shape, Draw)).isEqualTo(shape)
        }

        @ParameterizedTest(name = "Win: my shape is {1} over their shape {0}")
        @CsvSource(
            "Scissors,Rock",
            "Rock,Paper",
            "Paper,Scissors",
        )
        fun `should return shape for win `(theirShape: Shape, myShape: Shape) {
            assertThat(RockPaperScissorsGame.getMyShape(theirShape, Win)).isEqualTo(myShape)
        }

        @ParameterizedTest(name = "Lose: my shape is {1} to their {0}")
        @CsvSource(
            "Rock,Scissors",
            "Paper,Rock",
            "Scissors,Paper",
        )
        fun `should return shape for lose`(theirShape: Shape, myShape: Shape) {
            assertThat(RockPaperScissorsGame.getMyShape(theirShape, Lose)).isEqualTo(myShape)
        }
    }

}

