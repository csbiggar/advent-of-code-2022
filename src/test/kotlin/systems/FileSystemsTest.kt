package systems

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FileSystemsTest {

    @Test
    fun `should find size if only files`() {

        val input = """${'$'} cd /
${'$'} ls
dir a
200 b.txt
300 c.dat"""


        val result = calculate(input)

        assertThat(result).containsOnly(500)

    }

    @Test
    fun `should find size of parent and child directories`() {

        val input = """${'$'} cd /
${'$'} ls
dir a
200 b.txt
300 c.dat
${'$'} cd a
${'$'} ls
dir e
400 f
500 g"""


        val result = calculate(input)

        assertThat(result).containsOnly(900, 1400)

    }

    @Test
    fun `should find size of parent and child and grandchild directories`() {

        val input = """${'$'} cd /
${'$'} ls
dir a
200 b.txt
300 c.dat
${'$'} cd a
${'$'} ls
dir a
400 f
500 g
${'$'} cd a
${'$'} ls
600 h
"""


        val result = calculate(input)

        assertThat(result).containsOnly(600, 1500, 2000)

    }

    @Test
    fun `should find size of parent and two child directories`() {

        val input = """${'$'} cd /
${'$'} ls
dir a
200 b.txt
300 c.dat
dir b
${'$'} cd a
${'$'} ls
dir e
400 f
500 g
${'$'} cd ..
${'$'} cd b
${'$'} ls
dir e
600 f
700 g"""


        val result = calculate(input)

        assertThat(result).containsOnly(2700, 1300, 900)

    }

}

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

    println(directories)


    val split = input
        .split("${'$'} cd ")
        .filterNot { it.isBlank() }
        .reversed()

    val folders = split
        .map {
            sumDirectory(it.lines())
        }

    val result = mutableListOf<Long>()

    folders.indices.forEach {
        result.add(folders.drop(it).sum())
    }

    return result

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
    val subDirectories: List<Directory>)