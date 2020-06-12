import edu.illinois.cs.cs125.answerable.ExecutedTestStep
import edu.illinois.cs.cs125.answerable.TestGenerator
import edu.illinois.cs.cs125.answerable.TestingResults
import edu.illinois.cs.cs125.answerable.annotations.DEFAULT_EMPTY_NAME
import edu.illinois.cs.cs125.answerable.runTestsUnsecured
import junit.framework.Assert.assertTrue
import kotlin.random.Random

/**
 * Given a problem name, say "Problem", a list of names of "correct" submissions for that problem, and
 * a list of names of "incorrect" submissions for that problem, runs all the appropriate tests and assertions
 * to verify that the problem behaves as expected.
 *
 * Example usage with LastTen: testProblem("LastTen", listOf("correct"), listOf("badstatic")).
 *
 * If you have one class that provides multiple problems, you can specify one to use with the optional
 *   solutionName parameter.
 */
fun testProblem(
    problemName: String,
    corrects: List<String>,
    incorrects: List<String>,
    solutionName: String = DEFAULT_EMPTY_NAME
) {
    val reference = "$problemName.$problemName"

    corrects.forEach { attemptName ->
        val attempt = "$problemName.$attemptName.$problemName"
        val testResults = runAnswerableTest(attempt, reference, solutionName)
        assertTrue(
            "CDA did not pass for 'correct' attempt $attemptName",
            testResults.classDesignAnalysisResult.allMatch
        )
        val failedTests =
            testResults.testSteps.filterIsInstance<ExecutedTestStep>().filter { !it.succeeded }
        assertTrue(
            """
                Tests failed for 'correct' attempt $attemptName 
                An example of a failing input is: ${if (failedTests.isEmpty()) "" else failedTests[0].refOutput.args }
            """.trimIndent(),
            failedTests.isEmpty())
    }
    incorrects.forEach { attemptName ->
        val attempt = "$problemName.$attemptName.$problemName"
        val testResults = runAnswerableTest(attempt, reference, solutionName)
        if (testResults.classDesignAnalysisResult.allMatch) {
            assertTrue(
                "CDA and all tests passed for 'incorrect' attempt $attemptName",
                testResults.testSteps.filterIsInstance<ExecutedTestStep>().any { !it.succeeded }
            )
        }
    }
}

private fun String.load() = Class.forName(this)
/** Never call this function to run untrusted code. */
fun runAnswerableTest(
    submissionClass: String,
    solutionClass: String,
    solutionName: String = DEFAULT_EMPTY_NAME,
    randomSeed: Long = Random.nextLong()
): TestingResults {
    return TestGenerator(solutionClass.load(), solutionName)
        .loadSubmission(submissionClass.load())
        .runTestsUnsecured(randomSeed)
}