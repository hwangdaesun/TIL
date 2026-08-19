import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun `displayName returns name when user has name`() {
        val user = User(1, "Dustin")

        assertEquals("Dustin", user.displayName())
    }

    @Test
    fun `displayName returns fallback when name is null`() {
        val user = User(2, null)

        assertEquals("Unknown", user.displayName())
    }
}

