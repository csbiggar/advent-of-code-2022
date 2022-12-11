package communications

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommunicationsSystemTest {

    @Test
    fun `should report 7 for example requiring 4 characters`() {
        val result = reportAfterCharacter("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 4)
        assertThat(result).isEqualTo(7)
    }

    @Test
    fun `should report 5 for example requiring 4 characters`() {
        val result = reportAfterCharacter("bvwbjplbgvbhsrlpgdmjqwftvncz", 4)
        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `should report 10 for example requiring 4 characters`() {
        val result = reportAfterCharacter("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 4)
        assertThat(result).isEqualTo(10)
    }


    @Test
    fun `should report 19 for example requiring 14 characters`() {
        val result = reportAfterCharacter("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 14)
        assertThat(result).isEqualTo(19)
    }
}