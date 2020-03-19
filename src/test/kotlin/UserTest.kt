import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserTest {
    private val user = User(1, "Alice")

    @Test
    fun `should be able to increase reputation`() {
        user.changeReputation(10)

        Assertions.assertEquals(10, user.reputation)
    }

    @Test
    fun `should be able to decrease reputation`() {
        user.changeReputation(10)
        user.changeReputation(-5)

        Assertions.assertEquals(5, user.reputation)
    }
}