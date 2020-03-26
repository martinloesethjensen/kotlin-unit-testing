import org.amshove.kluent.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

class QuestionTest {

    private val user = User(1, "Alice")

    @Nested
    inner class `constructor should` {
        @Test
        fun `should throw exception if the title is empty`() {
            invoking { Question(1, user, "", "question") } `should throw` QuestionException::class
        }

        @Test
        fun `should throw exception if the question is empty`() {
            invoking { Question(1, user, "title", "") } `should throw` QuestionException::class
        }

        @Test
        fun `should not throw exception if the question is valid`() {
            invoking { Question(1, user, "title", "question") } `should not throw` QuestionException::class
        }

        @ParameterizedTest
        @CsvSource("' ', question", "'', question", "title, ' '", "title, ''")
        fun `throw an exception if title or question is invalid`(title: String, body: String) {
            invoking { Question(1, user, title, body) } `should throw` QuestionException::class
        }
    }

    @Nested
    @KotlinParameterizedTests
    inner class `constructor should with method source and annotation` {

        @Suppress("unused")
        fun titlesAndQuestions() = listOf(
            Arguments.of("", "question"),
            Arguments.of(" ", "question"),
            Arguments.of("title", ""),
            Arguments.of("title", " ")
        )

        @ParameterizedTest
        @MethodSource("titlesAndQuestions")
        fun `throw an exception if title or question is invalid`(title: String, question: String) {
            invoking { Question(1, user, title, question) } `should throw` QuestionException::class
        }
    }

    @Nested
    inner class answers {

        private val user = User(1, "Alice")
        private val question = Question(1, user, "title", "question")

        @Test
        fun `should have no answer`() {
            question.answers.shouldBeEmpty()
        }

        @Test
        fun `should have an answer`() {
            // Given
            val answer = Answer(1, user, "answer")

            // When
            question.addAnswer(answer)

            // Then
            question.answers.shouldNotBeEmpty()
        }

        @Test
        fun `should contain an answer`() {
            // Given
            val answer1 = Answer(1, user, "answer")
            val answer2 = Answer(2, user, "answer")

            // When
            question.addAnswer(answer1)
            question.addAnswer(answer2)

            // Then
            question.answers `should contain` answer1
        }

        @Test
        fun `should not contain an answer that was not added`() {
            // Given
            val answer1 = Answer(1, user, "answer")
            val answer2 = Answer(2, user, "answer")

            // When
            question.addAnswer(answer1)
            // answer2 is not added in the collection

            // Then
            question.answers `should not contain` answer2
        }
    }
}
