package systems

fun calculate(input: String): List<Long> {
    val lines = input.lines().filterNot { it.isEmpty() }.drop(1)

    var currentDirectory = Directory("/", emptyList(), emptyList())
    val directories = mutableMapOf<String, Directory>()

    println("starting directory $currentDirectory")

    for (line in lines) {
        when {
            isChangingIntoDirectory(line) -> {
                val name = line.substringAfter("${'$'} cd ")

                val fullyQualifiedName = getFullyQualifiedName(currentDirectory, name)

                val directory = Directory(name = fullyQualifiedName, files = emptyList(), subDirectories = emptyList())

                println("Change directory: $line Fully qualified name : $fullyQualifiedName")
                directories[fullyQualifiedName] = directory
                currentDirectory = directory
            }

            isSubdirectory(line) -> {

                val name = line.substringAfter(" ")
                val fullyQualifiedName = getFullyQualifiedName(currentDirectory, name)
                println("Subdirectory $line, fully qualified name $fullyQualifiedName")


                val subDirectory = directories.getOrDefault(fullyQualifiedName, Directory(name = fullyQualifiedName, files = emptyList(), subDirectories = emptyList()))
                directories[fullyQualifiedName] = subDirectory
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
                val updatedDirectory = currentDirectory.copy(files = currentDirectory.files + listOf(file))
                directories[currentDirectory.name] = updatedDirectory
            }
        }


    }




    TODO()

}

private fun getFullyQualifiedName(currentDirectory: Directory, name: String) = if (currentDirectory.name == "/") "/$name" else "${currentDirectory.name}/$name"

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

data class Directory(
    val name: String,
    val files: List<ElfFile>,
    val subDirectories: List<Directory>) {
    fun size() = files.sumOf { it.size }
    fun sizeIncludingSubDirectories(): Long {
        if (subDirectories.isEmpty()) {
            return size()
        }
        return subDirectories.sumOf { it.sizeIncludingSubDirectories() } + size()
    }
}