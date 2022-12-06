package rucksacks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
        assertThat(RucksackReoragnisation.getItemPriority(letter)).isEqualTo(value)
    }

    @ParameterizedTest(name = "Contents {0} has common letter {1}")
    @CsvSource(
        "vJrwpWtwJgWrhcsFMMfFFhFp,p",
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL,L",
        "PmmdzqPrVvPwwTWBwg,P"
    )
    fun `should find the common letter`(load: String, commonLetter: Char) {

        assertThat(RucksackReoragnisation.findCommonLetter(load)).isEqualTo(commonLetter)
    }


}