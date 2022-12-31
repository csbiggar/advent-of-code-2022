package systems

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rucksacks.CaloriesCalculator

class FileSystemsTest {

    @Test
    fun `should find size if only files`() {

        val input = """${'$'} cd /
${'$'} ls
dir a
200 b.txt
300 c.dat"""


        val result = calculateDirectorySizes(input)

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


        val result = calculateDirectorySizes(input)

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


        val result = calculateDirectorySizes(input)

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


        val result = calculateDirectorySizes(input)

        assertThat(result).containsOnly(2700, 1300, 900)

    }

    @Test
    fun `should find size of example`() {

        val input = """${'$'} cd /
${'$'} ls
dir a
14848514 b.txt
8504156 c.dat
dir d
${'$'} cd a
${'$'} ls
dir e
29116 f
2557 g
62596 h.lst
${'$'} cd e
${'$'} ls
584 i
${'$'} cd ..
${'$'} cd ..
${'$'} cd d
${'$'} ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k"""

        val result = calculateDirectorySizes(input)

        assertThat(result).containsOnly(584, 94853, 24933642, 48381165)

    }

    @Test
    fun `should sum smallest file sizes for the example`() {
        val input = """${'$'} cd /
${'$'} ls
dir a
14848514 b.txt
8504156 c.dat
dir d
${'$'} cd a
${'$'} ls
dir e
29116 f
2557 g
62596 h.lst
${'$'} cd e
${'$'} ls
584 i
${'$'} cd ..
${'$'} cd ..
${'$'} cd d
${'$'} ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k"""

        assertThat(findSumOfSmallFiles(input, 100000)).isEqualTo(95437)
    }

    @Test
    fun `should sum smallest file sizes for the real input`() {
        val input = CaloriesCalculator::class.java.getResource("/day-7.txt")!!.readText()

        // Part 1
        val result = findSumOfSmallFiles(input, 100000)

        assertThat(result).isEqualTo(1583951)
    }

}


class DirectoriesTest {

    @Test
    fun `no subdirectories`() {
        val dirA = Directory(DirectoryName("a"), listOf(ElfFile(1), ElfFile(2)), emptyList())

        val allDirectories = mapOf(
            dirA.name to dirA
        )

        assertThat(dirA.size()).isEqualTo(3)
        assertThat(dirA.sizeIncludingSubDirectories(allDirectories)).isEqualTo(3)
    }

    @Test
    fun `one subdirectory with no subdirectories`() {
        val dirB = Directory(DirectoryName("b"), listOf(ElfFile(3), ElfFile(2)), emptyList())
        val dirA = Directory(DirectoryName("a"), listOf(ElfFile(1), ElfFile(2)), listOf(dirB).map { it.name })
        val allDirectories = mapOf(
            dirA.name to dirA,
            dirB.name to dirB,
        )

        assertThat(dirA.size()).isEqualTo(3)
        assertThat(dirA.sizeIncludingSubDirectories(allDirectories)).isEqualTo(8)
    }

    @Test
    fun `one subdirectory with 2 subdirectories`() {
        val dirB = Directory(DirectoryName("b"), listOf(ElfFile(3), ElfFile(2)), emptyList())
        val dirC = Directory(DirectoryName("c"), listOf(ElfFile(4), ElfFile(2)), emptyList())
        val dirA = Directory(DirectoryName("a"), listOf(ElfFile(1), ElfFile(2)), listOf(dirB, dirC).map { it.name })

        val allDirectories = mapOf(
            dirA.name to dirA,
            dirB.name to dirB,
            dirC.name to dirC
        )

        assertThat(dirA.size()).isEqualTo(3)
        assertThat(dirA.sizeIncludingSubDirectories(allDirectories)).isEqualTo(14)
    }

    @Test
    fun `one subdirectory with 2 grandchildren`() {
        val dirB = Directory(DirectoryName("b"), listOf(ElfFile(3), ElfFile(2)), emptyList())
        val dirC = Directory(DirectoryName("c"), listOf(ElfFile(4), ElfFile(2)), emptyList())
        val dirZ = Directory(DirectoryName("z"), listOf(ElfFile(6)), listOf(dirB, dirC).map { it.name })
        val dirA = Directory(DirectoryName("a"), listOf(ElfFile(1), ElfFile(2)), listOf(dirZ).map { it.name })

        val allDirectories = mapOf(
            dirA.name to dirA,
            dirB.name to dirB,
            dirC.name to dirC,
            dirZ.name to dirZ,
        )

        assertThat(dirA.size()).isEqualTo(3)
        assertThat(dirA.sizeIncludingSubDirectories(allDirectories)).isEqualTo(20)
    }

    @Test
    fun `one subdirectory with 2 great grandchildren`() {
        val dirB = Directory(DirectoryName("b"), listOf(ElfFile(3), ElfFile(2)), emptyList())
        val dirC = Directory(DirectoryName("c"), listOf(ElfFile(4), ElfFile(2)), emptyList())
        val dirZ = Directory(DirectoryName("z"), listOf(ElfFile(6)), listOf(dirB, dirC).map { it.name })
        val dirX = Directory(DirectoryName("x"), listOf(ElfFile(1)), listOf(dirZ).map { it.name })
        val dirA = Directory(DirectoryName("a"), listOf(ElfFile(1), ElfFile(2)), listOf(dirX).map { it.name })

        val allDirectories = mapOf(
            dirA.name to dirA,
            dirB.name to dirB,
            dirC.name to dirC,
            dirZ.name to dirZ,
            dirX.name to dirX,
        )

        assertThat(dirA.size()).isEqualTo(3)
        assertThat(dirA.sizeIncludingSubDirectories(allDirectories)).isEqualTo(21)
    }


}