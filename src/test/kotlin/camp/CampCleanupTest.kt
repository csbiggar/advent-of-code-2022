package camp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CampCleanupTest {

    private val input = """2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8""".lines()


    @ParameterizedTest(name = "Assignment {0} is duplicated? {1}")
    @CsvSource(
        delimiter = '|',
        value = [
            "2-8,3-7|true",
            "22-99,98-99|true",
            "1-94,93-98|false",
            "3-3,2-80|true",
            "4-99,1-1|false",
        ]
    )
    fun `should decide if one assignment is contained entirely within the other`(assignmentPair: String, isDuplicated: Boolean) {
        assertThat(CampCleanup.assignmentIsDuplicated(assignmentPair)).isEqualTo(isDuplicated)
    }

    @Test
    fun `should convert range to list of numbers`() {
        val result = CampCleanup.explodeNumbers("2-5")
        assertThat(result).containsExactly("2", "3", "4", "5")
    }

    @Test
    fun `should find duplicated assignments`() {
        val result = CampCleanup(input).findDuplicatedAssignments()

        assertThat(result).containsExactly(
            "2-8,3-7",
            "6-6,4-6"
        )
    }

    @ParameterizedTest(name = "Assignment {0} is overlapping? {1}")
    @CsvSource(
        delimiter = '|',
        value = [
            "2-8,3-7|true",
            "5-7,7-9|true",
            "4-99,1-1|false",
        ]
    )
    fun `should decide if one assignment overlaps with the other`(assignmentPair: String, isOverlapping: Boolean) {
        assertThat(CampCleanup.assignmentsOverlap(assignmentPair)).isEqualTo(isOverlapping)
    }

}