package forest

import rucksacks.CaloriesCalculator

fun main() {
    val input = CaloriesCalculator::class.java.getResource("/day-8.txt")!!.readText()

    val forest = Forest.fromInput(input)

    println(forest.countVisibleTrees())
    println(forest.maxScenicScore())
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

    private fun canView(tree: Tree) = canView(tree.x, tree.y)

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

    private fun getScenicScore(tree: Tree): Int = getScenicScore(tree.x, tree.y)

    fun getScenicScore(x: Int, y: Int): Int {

        val thisTree = trees.single { it.x == x && it.y == y }

        val col = trees.filter { it.x == x }
        val up = col.subList(0, y).reversed()
        val down = col.subList(y + 1, maxIndex + 1)

        val row = trees.filter { it.y == y }
        val left = row.subList(0, x).reversed()
        val right = row.subList(x + 1, maxIndex + 1)


        return up.viewingDistanceForHeight(thisTree.height) *
                down.viewingDistanceForHeight(thisTree.height) *
                left.viewingDistanceForHeight(thisTree.height) *
                right.viewingDistanceForHeight(thisTree.height)
    }

    private fun List<Tree>.viewingDistanceForHeight(allowedHeight: Int): Int {
        val indexOfFirstBlockingTree = indexOfFirst { it.height >= allowedHeight }
        return if (indexOfFirstBlockingTree == -1) this.size else indexOfFirstBlockingTree + 1
    }

    fun countVisibleTrees(): Int {
        return trees
            .map {
                canView(it)
            }
            .count { it }
    }

    fun maxScenicScore(): Int {
        return trees.maxOf {
            getScenicScore(it)
        }
    }

    private fun List<Tree>.allSmallerThan(thisTree: Tree): Boolean {
        return map { it.height }.all { it < thisTree.height }
    }

}

data class Tree(val x: Int, val y: Int, val height: Int)