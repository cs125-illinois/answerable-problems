import org.junit.jupiter.api.Test

class TestProblems {
    @Test
    fun `LastTen should work properly`() {
        testProblem("LastTen", listOf("correct"), listOf("badstatic"))
    }
}