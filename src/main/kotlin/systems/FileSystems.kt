package systems

fun calculate(input: String): List<Long> {
    val lines = input.lines().filterNot { it.isEmpty() }.drop(1)

    var currentDirectory = Directory(DirectoryName("/"), emptyList(), emptyList())
    val allDirectories: MutableMap<DirectoryName, Directory> = mutableMapOf(currentDirectory.name.copy() to currentDirectory.copy())

    println("starting directory $currentDirectory")

    for (line in lines) {
        when {
            isChangingIntoDirectory(line) -> {
                val name = line.substringAfter("${'$'} cd ")

                val newPosition = getFullyQualifiedName(currentDirectory, name)

                val directory = Directory(name = newPosition, files = emptyList(), subDirectories = emptyList())

                println("Change directory: $line Fully qualified name : $newPosition")
                allDirectories[newPosition] = directory
                currentDirectory = directory
            }

            isSubdirectory(line) -> {
                val name = line.substringAfter(" ")
                val fullyQualifiedName = getFullyQualifiedName(currentDirectory, name)
                println("Subdirectory $line, fully qualified name $fullyQualifiedName")

                val subDirectory = allDirectories.getOrDefault(fullyQualifiedName, Directory(name = fullyQualifiedName, files = emptyList(), subDirectories = emptyList()))
                allDirectories[fullyQualifiedName] = subDirectory

                currentDirectory = currentDirectory.copy(subDirectories = currentDirectory.subDirectories + listOf(subDirectory.name))
                allDirectories[currentDirectory.name] = currentDirectory

                println("Added this directory to current directory ${currentDirectory.name}'s subdirectories: ${allDirectories[currentDirectory.name]}")
            }

            isChangingUpDirectory(line) -> {
                // TODO
                println("changing up a directory $line")
            }

            isListContents(line) -> {
                // TODO
                println("List $line")
            }

            else -> {
                // it's a file
                println("File $line")

                val file = ElfFile(line.substringBefore(" ").toLong())
                currentDirectory = currentDirectory.copy(files = currentDirectory.files + listOf(file))
                allDirectories[currentDirectory.name] = currentDirectory
            }
        }


    }


    allDirectories.forEach { (_, directory) ->
        println("size [${directory.size()}] full size [${directory.sizeIncludingSubDirectories(allDirectories)}] $directory")
    }

    return allDirectories.values.map { it.sizeIncludingSubDirectories(allDirectories) }.filterNot { it == 0L }

}

private fun getFullyQualifiedName(currentDirectory: Directory, name: String): DirectoryName {
    val stringName = if (currentDirectory.name.id == "/") "/$name" else "${currentDirectory.name.id}/$name"
    return DirectoryName(stringName)
}

private fun isChangingUpDirectory(line: String) = line == "${'$'} cd .."

private fun isListContents(line: String) = line == "${'$'} ls"

private fun isChangingIntoDirectory(line: String) = line.startsWith("${'$'} cd") && line != "${'$'} cd .."

private fun sumDirectory(lines: List<String>) = lines
    .filterNot {
        it.startsWith("${'$'}")
                || isSubdirectory(it)
                || it.isBlank()
                || !it.contains(" ")
    }
    .sumOf { it.substringBefore(" ").toLong() }

private fun isSubdirectory(line: String) = line.startsWith("dir")


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