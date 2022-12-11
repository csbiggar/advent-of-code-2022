package communications

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommunicationsSystemTest {

    @Test
    fun `should report 7 for example`() {
        val result = reportAfterCharacter("mjqjpqmgbljsphdztnvjfqwrcgsmlb")
        assertThat(result).isEqualTo(7)
    }

    @Test
    fun `should report 5 for example`() {
        val result = reportAfterCharacter("bvwbjplbgvbhsrlpgdmjqwftvncz")
        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `should report 10 for example`() {
        val result = reportAfterCharacter("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
        assertThat(result).isEqualTo(10)
    }
}