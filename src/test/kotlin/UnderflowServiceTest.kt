import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

class UnderflowServiceTest {

    private val mockQuestionRepository = mockk<IQuestionRepository>()
    private val mockUserRepository = mockk<IUserRepository>()

    private val service = UnderflowService(mockQuestionRepository, mockUserRepository)

    private val questionId = 20
    private val voterId = 10

    @Test
    fun `should be able to initialize service`() {
        service.shouldNotBeNull()
    }

    @Test
    fun `should be able to vote up question`() {
        // Given
        val user = User(1, "Alice")
        val question = Question(questionId, user, "title", "question")

        // When
        user.changeReputation(3000) // Allowed to vote

        // Question gets two votes --> vote count should now be 2
        question.voteUp()
        question.voteUp()

        // Initialize the mocks
        every { mockQuestionRepository.findQuestion(questionId) } returns question
        every { mockUserRepository.findUser(voterId) } returns user
        every { mockUserRepository.findUser(question.userId) } returns user
        every { mockQuestionRepository.update(question) } just Runs
        every { mockUserRepository.update(user) } just Runs

        // We vote up the question once --> vote count should now be 3
        val votes = service.voteUpQuestion(questionId, voterId)

        // Then
        votes `should be` 3
    }
}