package systems

import rucksacks.CaloriesCalculator


fun main() {
    val input = CaloriesCalculator::class.java.getResource("/day-7.txt")!!.readText()

    // Part 1
    val result = findSumOfSmallFiles(input, 100000)
    println(result)

    // Part 2
    val sorted = calculateDirectorySizes(input).sorted()
    val spaceRequired  = 30_000_000 - (70_000_000 - sorted.last())
    val smallestSuitableDirectorySize = sorted.first { it >= spaceRequired }
    println(smallestSuitableDirectorySize)

}

fun findSumOfSmallFiles(input: String, maxSize: Long): Long {
    return calculateDirectorySizes(input).filter { it <= maxSize }.sum()
}

fun calculateDirectorySizes(input: String): List<Long> {
    val lines = input.lines().filterNot { it.isEmpty() }.drop(1)

    val rootName = DirectoryName("/")
    val allDirectories: MutableMap<DirectoryName, Directory> = mutableMapOf(rootName to Directory(rootName, emptyList(), emptyList()))
    val position: MutableSet<DirectoryName> = mutableSetOf(rootName)

    for (line in lines) {
        when {
            changeDirectory(line) -> {
                val name = line.substringAfter("${'$'} cd ")

                val newPosition = getFullyQualifiedName(position.last(), name)

                println("Change directory: $line Fully qualified name : $newPosition")
                position.add(newPosition)
            }

            subdirectoryName(line) -> {
                val relativeName = line.substringAfter(" ")
                val name = getFullyQualifiedName(position.last(), relativeName)
                println("Subdirectory $line, fully qualified name $name")

                val subDirectory = Directory(name = name, files = emptyList(), subDirectories = emptyList())
                allDirectories[name] = subDirectory

                val currentDirectory = allDirectories[position.last()]!!
                allDirectories[position.last()] = currentDirectory.copy(subDirectories = currentDirectory.subDirectories + listOf(subDirectory.name))

                println("Added this directory to current directory ${currentDirectory.name}'s subdirectories: ${allDirectories[currentDirectory.name]}")
            }

            changeUpDirectory(line) -> {
                position.remove(position.last())
            }

            listContents(line) -> {
                // TODO
                println("List $line")
            }

            else -> {
                // it's a file
                println("File $line")

                val file = ElfFile(line.substringBefore(" ").toLong())
                val currentDirectory = allDirectories[position.last()]!!
                allDirectories[currentDirectory.name] = currentDirectory.copy(files = currentDirectory.files + listOf(file))
            }
        }
    }


    allDirectories.forEach { (name, directory) ->
        println("${name.id} :size [${directory.size()}] full size [${directory.sizeIncludingSubDirectories(allDirectories)}] $directory")
    }

    return allDirectories.values.map { it.sizeIncludingSubDirectories(allDirectories) }.filterNot { it == 0L }

}

private fun getFullyQualifiedName(currentDirectory: DirectoryName, name: String): DirectoryName {
    val stringName = if (currentDirectory.id == "/") "/$name" else "${currentDirectory.id}/$name"
    return DirectoryName(stringName)
}

private fun changeUpDirectory(line: String) = line == "${'$'} cd .."

private fun listContents(line: String) = line == "${'$'} ls"

private fun changeDirectory(line: String) = line.startsWith("${'$'} cd") && line != "${'$'} cd .."

private fun sumDirectory(lines: List<String>) = lines
    .filterNot {
        it.startsWith("${'$'}")
                || subdirectoryName(it)
                || it.isBlank()
                || !it.contains(" ")
    }
    .sumOf { it.substringBefore(" ").toLong() }

private fun subdirectoryName(line: String) = line.startsWith("dir")


data class ElfFile(val size: Long)
data class DirectoryName(val id: String)

data class Directory(
    val name: DirectoryName,
    val files: List<ElfFile>,
    val subDirectories: List<DirectoryName>) {
    fun size() = files.sumOf { it.size }
    fun sizeIncludingSubDirectories(allDirectories: Map<DirectoryName, Directory>): Long {
        if (subDirectories.isEmpty()) {
            return size()
        }
        return subDirectories.sumOf { allDirectories[it]!!.sizeIncludingSubDirectories(allDirectories) } + size()
    }
}