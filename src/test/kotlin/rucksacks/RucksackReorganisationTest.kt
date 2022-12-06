package rucksacks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import rucksacks.RucksackReorganisation.splitIntoThrees

class RucksackReorganisationTest {

    private val input = """vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw"""


    @ParameterizedTest(name = "Letter {0} has value {1}")
    @CsvSource(
        "a,1",
        "A,27",
        "Z,52"
    )
    fun `should get letter value`(letter: Char, value: Int) {
        assertThat(RucksackReorganisation.getItemPriority(letter)).isEqualTo(value)
    }

    @ParameterizedTest(name = "Contents {0} has common letter {1}")
    @CsvSource(
        "vJrwpWtwJgWrhcsFMMfFFhFp,p",
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL,L",
        "PmmdzqPrVvPwwTWBwg,P"
    )
    fun `should find the common letter`(load: String, commonLetter: Char) {

        assertThat(RucksackReorganisation.findCommonLetter(load)).isEqualTo(commonLetter)
    }

    @Test
    fun `should find the letter common to three lines`() {
        val rucksacksGroup1 = """vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg""".lines()

        val rucksacksGroup2 = """wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMD""".lines()

        assertThat(RucksackReorganisation.findCommonItem(rucksacksGroup1)).isEqualTo('r')
        assertThat(RucksackReorganisation.findCommonItem(rucksacksGroup2)).isEqualTo('Z')

    }

    @Test
    fun `should split into groups of three`() {
        val lines = (0..8).map { index -> "line_$index"}

        assertThat(lines.splitIntoThrees()).containsExactly(
            listOf("line_0", "line_1", "line_2"),
            listOf("line_3", "line_4", "line_5"),
            listOf("line_6", "line_7", "line_8"),
        )
    }


}