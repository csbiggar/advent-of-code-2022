package camp

fun main() {
    val input = CampCleanup::class.java.getResource("/day-4.txt")!!.readText().lines()

    println(CampCleanup(input).findDuplicatedAssignments().size)
    println(CampCleanup(input).findOverlappingAssignments().size)
}

class CampCleanup(private val assignments: List<String>) {

    fun findDuplicatedAssignments(): List<String> {
        return assignments
            .filter {
                assignmentIsDuplicated(it)
            }
    }

    fun findOverlappingAssignments(): List<String> {
        return assignments
            .filter {
                assignmentsOverlap(it)
            }
    }

    companion object {
        fun explodeNumbers(range: String): List<String> {
            val start = range.substringBefore("-").toInt()
            val end = range.substringAfter("-").toInt()

            return (start..end).map { it.toString() }
        }

        fun assignmentIsDuplicated(assignmentPair: String): Boolean {
            val assignment1 = explodeNumbers(assignmentPair.substringBefore(","))
            val assignment2 = explodeNumbers(assignmentPair.substringAfter(","))

            return assignment1.containsAll(assignment2) || assignment2.containsAll(assignment1)
        }

        fun assignmentsOverlap(assignmentPair: String): Boolean {
            val assignment1 = explodeNumbers(assignmentPair.substringBefore(","))
            val assignment2 = explodeNumbers(assignmentPair.substringAfter(","))

            return assignment1.intersect(assignment2.toSet()).isNotEmpty()
        }
    }
}