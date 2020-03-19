import org.amshove.kluent.`should not throw`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
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
}