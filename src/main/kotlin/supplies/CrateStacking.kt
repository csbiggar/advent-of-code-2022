package supplies

import rucksacks.CaloriesCalculator

fun main() {
    val input = CaloriesCalculator::class.java.getResource("/day-5.txt")!!.readText()

    val stacks = extractStartingStacks(input)
    val instructions = extractInstructions(input)

    println(stacks)
    println(instructions)
    stacks.rearrangeCrates(instructions)

    println(getTopCrate(stacks).joinToString(""))

}


fun List<Stack>.rearrangeCrates(instructions: List<CratesMove>) {
    instructions.forEach { move ->
        val fromStack = single { it.id == move.fromStack }
        val toStack = single { it.id == move.toStack }

        val cratesToMove = fromStack.crates.takeLast(move.numberToMove)

        toStack.crates.addAll(cratesToMove)
        fromStack.crates.removeLast(move.numberToMove)
    }
}

fun getTopCrate(stacks: List<Stack>): List<Char> {
    return stacks.map { it.crates.last() }
}

private fun MutableList<Char>.removeLast(howMany: Int) {
    repeat(howMany) {
        this.removeLast()
    }
}

fun extractStartingStacks(input: String): List<Stack> {
    val rows = input.substringBefore("\n\n").lines().reversed()

    val stacks = rows.first()
        .trim()
        .split("   ")
        .map { Stack(it.toInt(), mutableListOf()) }

    rows.drop(1).forEach { verticalPosition ->
        verticalPosition
            .windowed(4, 4, partialWindows = true)
            .forEachIndexed { index, crateEntry ->
                val crate = crateEntry[1]
                if (crate != ' ') {
                    stacks.single { it.id == index + 1 }.crates.add(crate)
                }
            }
    }

    return stacks
}

fun extractInstructions(input: String): List<CratesMove> {
    return input.substringAfter("\n\n")
        .lines()
        .map {
            CratesMove(
                fromStack = it.substringAfter("from ").substringBefore(" to").toInt(),
                toStack = it.substringAfterLast(" ").toInt(),
                numberToMove = it.substringAfter("move ").substringBefore(" from").toInt()
            )
        }
}

data class CratesMove(val fromStack: Int, val toStack: Int, val numberToMove: Int)

data class Stack(val id: Int, val crates: MutableList<Char>)