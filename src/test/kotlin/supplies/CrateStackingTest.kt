package supplies

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CrateStackingTest {

    private val example = """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2""".trimIndent()

    @Test
    fun `should rearrange crates`() {
        val stacks = listOf(
            Stack(1, mutableListOf('Z', 'N')),
            Stack(2, mutableListOf('M', 'C', 'D')),
            Stack(3, mutableListOf('P')),
        )

        val instructions = listOf(
            CratesMove(fromStack = 2, toStack = 1, numberToMove = 1),
            CratesMove(fromStack = 1, toStack = 3, numberToMove = 3),
            CratesMove(fromStack = 2, toStack = 1, numberToMove = 2),
            CratesMove(fromStack = 1, toStack = 2, numberToMove = 1),
        )

        stacks.rearrangeCrates(instructions)

        assertThat(stacks).containsExactly(
            Stack(1, mutableListOf('M')),
            Stack(2, mutableListOf('C')),
            Stack(3, mutableListOf('P', 'Z', 'N', 'D')),
        )
    }

    @Test
    fun `should give the top crate of each stack`() {
        val stacks = listOf(
            Stack(1, mutableListOf('Z', 'N')),
            Stack(2, mutableListOf('M', 'C', 'D')),
            Stack(3, mutableListOf('P')),
        )

        val result = getTopCrate(stacks)

        assertThat(result).containsExactly('N', 'D', 'P')

    }

    @Test
    fun `should extract starting stack`() {
        val stacks = extractStartingStacks(example)
        assertThat(stacks).containsExactly(
            Stack(1, mutableListOf('Z', 'N')),
            Stack(2, mutableListOf('M', 'C', 'D')),
            Stack(3, mutableListOf('P')),
        )
    }

    @Test
    fun `should extract starting stacks when middle stack has no crate`() {

        val missingCrate = """[Z]     [P]
 1   2   3 

move 1 from 2 to 1"""

        val stacks = extractStartingStacks(missingCrate)
        assertThat(stacks).containsExactly(
            Stack(1, mutableListOf('Z')),
            Stack(2, mutableListOf()),
            Stack(3, mutableListOf('P')),
        )
    }

    @Test
    fun `should extract instructions`() {
        val instructions = extractInstructions(example)
        assertThat(instructions).containsExactly(
            CratesMove(fromStack = 2, toStack = 1, numberToMove = 1),
            CratesMove(fromStack = 1, toStack = 3, numberToMove = 3),
            CratesMove(fromStack = 2, toStack = 1, numberToMove = 2),
            CratesMove(fromStack = 1, toStack = 2, numberToMove = 1),
        )
    }

    @Test
    fun `should handle larger numbers in instructions`() {
        val bigNumbers = """

move 241 from 456 to 1"""

        val instructions = extractInstructions(bigNumbers)
        assertThat(instructions).containsExactly(
            CratesMove(fromStack = 456, toStack = 1, numberToMove = 241),
        )
    }
}