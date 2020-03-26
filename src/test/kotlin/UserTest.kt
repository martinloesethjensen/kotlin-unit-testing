import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserTest {
    private val user = User(1, "Alice")

    @Test
    fun `should be able to increase reputation`() {
        user.changeReputation(10)

        user.reputation `should be` 10
    }

    @Test
    fun `should be able to decrease reputation`() {
        user.changeReputation(10)
        user.changeReputation(-5)

        user.reputation `should be` 5
    }

    @Nested
    inner class `post should be able to` {

        private val editReputationLimit = 2000

        @Test
        fun `edit if reputation is greater than 2000`() {
            user.changeReputation(editReputationLimit + 1)
            user.canEditPost().shouldBeTrue()
        }

        @Test
        fun `edit if reputation is equal to 2000`() {
            user.changeReputation(editReputationLimit)
            user.canEditPost().shouldBeFalse()
        }

        @Test
        fun `edit if reputation is less than 2000`() {
            user.changeReputation(editReputationLimit - 1)
            user.canEditPost().shouldBeFalse()
        }
    }
}
