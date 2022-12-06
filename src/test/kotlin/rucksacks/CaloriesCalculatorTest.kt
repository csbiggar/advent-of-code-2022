package rucksacks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CaloriesCalculatorTest {

    private val input = """1000
2000
3000

4000

5000
6000

7000
8000
9000

10000"""

    @Test
    fun `should find highest calories`(){
       assertThat(CaloriesCalculator(input).findHighestCalories()).isEqualTo(24000)
    }

    @Test
    fun `should find top three calories`(){
       assertThat(CaloriesCalculator(input).findCaloriesPacks().take(3)).containsExactly(
           24000, 11000, 10000
       )
    }



}