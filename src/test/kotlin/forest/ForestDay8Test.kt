package forest

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class ForestDay8Test {
    val exampleInput = """30373
25512
65332
33549
35390"""

    @Nested
    inner class VisibleTrees {
        @Test
        fun `map input to trees`() {

            val input = """12
34"""
            val result = Forest.fromInput(input)

            assertThat(result.trees).containsOnly(
                Tree(0, 0, 1),
                Tree(0, 1, 3),
                Tree(1, 0, 2),
                Tree(1, 1, 4),

                )
        }

        @ParameterizedTest(name = "Tree height [{0}] can be seen? [{1}] in forest of height 2")
        @CsvSource(
            delimiter = '|',
            value = [
                "1|false",
                "2|false",
                "3|true",
            ]
        )
        fun `can see middle tree`(treeHeight: Int, canSeeTree: Boolean) {

            val input = """222
2${treeHeight}2
222"""
            val forest = Forest.fromInput(input)

            assertThat(forest.canView(1, 1)).isEqualTo(canSeeTree)
        }


        @Test
        fun `can see tree when there are taller trees but not in direct line of sight`() {

            val input = """2222
            |2322
            |2222
            |2225
        """.trimMargin()
            val forest = Forest.fromInput(input)

            assertThat(forest.canView(1, 1)).isTrue()
        }

        @Test
        fun `can see tree when it's blocked vertically but not horizontally`() {

            val input = """2222
            |2322
            |2522
            |2225
        """.trimMargin()
            val forest = Forest.fromInput(input)

            assertThat(forest.canView(1, 1)).isTrue()
        }

        @Test
        fun `can see tree when it's blocked in 3 directions but clear in the fourth`() {

            val input = """5555
            |5321
            |5555
            |5555
        """.trimMargin()
            val forest = Forest.fromInput(input)

            assertThat(forest.canView(1, 1)).isTrue()
        }

        @Test
        fun `edge trees are always visibly`() {

            val input = """111
            |555
            |111
        """.trimMargin()
            val forest = Forest.fromInput(input)

            assertThat(forest.canView(0, 0)).isTrue()
            assertThat(forest.canView(2, 2)).isTrue()
        }

        @Test
        fun `count visible trees`() {

            val forest = Forest.fromInput(
                """30373
            |25512
            |65332
            |33549
            |35390""".trimMargin()
            )

            assertThat(forest.countVisibleTrees()).isEqualTo(21)
        }

        @Test
        fun `bottom left is visible `() {

            val forest = Forest.fromInput(
                """30373
            |25512
            |65332
            |33549
            |35390""".trimMargin()
            )

            assertThat(forest.canView(2, 3)).isTrue()
        }
    }

    @Nested
    inner class ScenicScore {
        private val forest = Forest.fromInput(
            """30373
            |25512
            |65332
            |33549
            |35390""".trimMargin()
        )

        @Test
        fun `should get individual scenic score`() {
            val score = forest.getScenicScore(2, 1)
            assertThat(score).isEqualTo(4)
        }

        @Test
        fun `should find max scenic score`(){
            assertThat(forest.maxScenicScore()).isEqualTo(8)
        }


    }


}