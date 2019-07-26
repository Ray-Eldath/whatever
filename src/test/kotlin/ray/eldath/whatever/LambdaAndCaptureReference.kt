package ray.eldath.whatever

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

object LambdaAndCaptureReference {
    private val numbers = listOf(1, 3, 7, 9, 8)
    private val expected = numbers.sum()

    @RepeatedTest(5)
    fun testClosure() {
        var capture = 0

        numbers.parallelStream()
            .forEach {
                Thread.sleep(100)
                capture += it
            }

        assertEquals(expected, capture)
    }

    @RepeatedTest(5)
    fun testReduceLambda() {
        assertEquals(
            expected,
            numbers.parallelStream()
                .reduce(0) { sum: Int, e: Int ->
                    Thread.sleep(100)
                    return@reduce sum + e
                }
        )
    }

    @Test
    fun testMixingParadigms() {
        val functions = arrayListOf<() -> Unit>()
        for (i in 1..3)
            functions += { println("value is $i") }

        functions.forEach { it() }
    }
}