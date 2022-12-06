package rucksacks

import rucksacks.RucksackReoragnisation.findCommonLetter
import rucksacks.RucksackReoragnisation.getItemPriority


fun main() {
    val input = RucksackReoragnisation::class.java.getResource("/day-3.txt")!!.readText()

    val sumOfPriorities = input.lines()
        .sumOf {
        getItemPriority(findCommonLetter(it))
    }

    println(sumOfPriorities)

}

object RucksackReoragnisation {

    private val valuesMap = prepareValues()

    fun getItemPriority(char: Char): Int {
        return valuesMap[char]!!
    }

    fun findCommonLetter(load: String): Char {
        val compartmentSize = load.length / 2
        val contents1 = load.substring(0, compartmentSize)
        val contents2 = load.substringAfter(contents1)

        return contents1.toSet().intersect(contents2.toSet()).single()
    }

    private fun prepareValues(): Map<Char, Int> {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        val lowerCaseValues = alphabet
            .mapIndexed { index, c -> c to index + 1 }
            .toMap()

        val upperCaseValues = alphabet.uppercase()
            .mapIndexed { index, c -> c to index + 27 }
            .toMap()
        return lowerCaseValues + upperCaseValues
    }
}