package communications

import rucksacks.CaloriesCalculator

fun main() {
    val input = CaloriesCalculator::class.java.getResource("/day-6.txt")!!.readText()

    println(reportAfterCharacter(input, 4))
    println(reportAfterCharacter(input, 14))

}

fun reportAfterCharacter(input: String, distinctCharactersRequired: Int): Int {
    val indexOfFirst = input
        .windowed(distinctCharactersRequired, 1)
        .indexOfFirst {
            it.toCharArray().distinct().size == distinctCharactersRequired
        }
    return indexOfFirst + distinctCharactersRequired
}