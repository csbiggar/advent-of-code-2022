package communications

import rucksacks.CaloriesCalculator

fun main() {
    val input = CaloriesCalculator::class.java.getResource("/day-6.txt")!!.readText()

    println(reportAfterCharacter(input))

}

fun reportAfterCharacter(input: String): Int {
    val indexOfFirst = input
        .windowed(4, 1)
        .indexOfFirst {
            it.toCharArray().distinct().size == 4
        }
    return indexOfFirst + 4
}