package forest

import rucksacks.CaloriesCalculator

fun main() {
    val input = CaloriesCalculator::class.java.getResource("/day-8.txt")!!.readText()

    val forest = Forest.fromInput(input)

    println(forest.countVisibleTrees())
}

class Forest(val trees: List<Tree>) {
    private val maxIndex: Int = trees.maxOf { it.x }

    companion object {
        fun fromInput(input: String): Forest {
            val trees = input.lines()
                .mapIndexed { y, row ->
                    row.mapIndexed { x, height ->
                        Tree(x, y, height.digitToInt())
                    }
                }
                .flatten()

            return Forest(trees)
        }
    }

    private fun canView(tree: Tree): Boolean {
        return canView(tree.x, tree.y)
    }

    fun canView(x: Int, y: Int): Boolean {

        if (x == 0 || y == 0 || x == maxIndex || y == maxIndex) return true

        val thisTree = trees.single { it.x == x && it.y == y }

        val col = trees.filter { it.x == x }
        val up = col.subList(0, y)
        val down = col.subList(y + 1, maxIndex + 1)

        val row = trees.filter { it.y == y }
        val left = row.subList(0, x)
        val right = row.subList(x + 1, maxIndex + 1)

        return up.allSmallerThan(thisTree)
                || down.allSmallerThan(thisTree)
                || left.allSmallerThan(thisTree)
                || right.allSmallerThan(thisTree)
    }

    fun countVisibleTrees(): Int {
        return trees.map {

            val canView = canView(it)
            println("$it, visible [$canView]")
            canView

        }.count { it }
    }

    private fun List<Tree>.allSmallerThan(thisTree: Tree): Boolean {
        return map { it.height }.all { it < thisTree.height }
    }

}

data class Tree(val x: Int, val y: Int, val height: Int)