package rucksacks

fun main() {

    val input = CaloriesCalculator::class.java.getResource("/day-1.txt")!!.readText()

    val caloriesCalculator = CaloriesCalculator(input)

    println(caloriesCalculator.findHighestCalories())
    println(caloriesCalculator.findCaloriesPacks().take(3).sum())

}


class CaloriesCalculator(private val input: String) {

    fun findHighestCalories(): Long {
        return findCaloriesPacks().maxOf { it }
    }



    fun findCaloriesPacks() = input
        .split("\n\n")
        .map { it.lines().sumOf { it.toLong() } }
        .sortedDescending()
}