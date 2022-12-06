package rucksacks

import rucksacks.RucksackReorganisation.findCommonItem
import rucksacks.RucksackReorganisation.findCommonLetter
import rucksacks.RucksackReorganisation.getItemPriority
import rucksacks.RucksackReorganisation.splitIntoThrees


fun main() {
    val rucksacks = RucksackReorganisation::class.java.getResource("/day-3.txt")!!.readText().lines()

    val sumOfPriorities = rucksacks
        .sumOf {
            getItemPriority(findCommonLetter(it))
        }

    println(sumOfPriorities)

    val groupedPriorities = rucksacks
        .splitIntoThrees()
        .sumOf {
            getItemPriority(findCommonItem(it))
        }

    println(groupedPriorities)
}

object RucksackReorganisation {

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

    fun findCommonItem(rucksacks: List<String>): Char {
        if (rucksacks.size != 3) throw Exception("Whoops! wrong number of rucksacks in group, $rucksacks")
        return rucksacks[0].toSet()
            .intersect(rucksacks[1].toSet())
            .intersect(rucksacks[2].toSet())
            .single()
    }

    fun List<String>.splitIntoThrees(): List<List<String>> = windowed(3,3)

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