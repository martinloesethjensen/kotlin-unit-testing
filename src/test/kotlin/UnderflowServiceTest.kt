import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class UnderflowServiceTest {

    @Nested
    inner class UnderflowService {
        private val mockQuestionRepository = mockk<IQuestionRepository>()
        private val mockUserRepository = mockk<IUserRepository>(relaxUnitFun = true)

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
//            every { mockUserRepository.update(user) } just Runs

            // We vote up the question once --> vote count should now be 3
            val votes = service.voteUpQuestion(questionId, voterId)

            // Then
            votes `should be` 3
        }

        @Test
        fun `should throw an exception id the question id is invalid`() {
            // Given
            val user = User(1, "Alice")
            val question = Question(2, user, "title", "question")

            // When
            every { mockQuestionRepository.findQuestion(questionId) } throws Exception()

            // Then
            invoking { service.voteUpQuestion(questionId, voterId) } `should throw` ServiceException::class
        }
    }

    @Nested
    inner class WithAnnotation {
        @MockK
        private lateinit var mockQuestionRepository: IQuestionRepository

        @RelaxedMockK
        private lateinit var mockUserRepository: IUserRepository

        init {
            MockKAnnotations.init(this)
        }

        private val service = UnderflowService(mockQuestionRepository, mockUserRepository)

        private val questionId = 20
        private val voterId = 10

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
//            every { mockUserRepository.update(user) } just Runs

            // We vote up the question once --> vote count should now be 3
            val votes = service.voteUpQuestion(questionId, voterId)

            // Then
            votes `should be` 3
        }
    }

    @Nested
    @ExtendWith(MockKExtension::class)
    inner class WithAnnotationUsingJUnit {
        @MockK
        private lateinit var mockQuestionRepository: IQuestionRepository

        @RelaxedMockK
        private lateinit var mockUserRepository: IUserRepository

        private val questionId = 20
        private val voterId = 10

        @Test
        fun `should be able to vote up question`() {
            // Given
            val service = UnderflowService(mockQuestionRepository, mockUserRepository)
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
//            every { mockUserRepository.update(user) } just Runs

            // We vote up the question once --> vote count should now be 3
            val votes = service.voteUpQuestion(questionId, voterId)

            // Then
            votes `should be` 3
        }
    }
}