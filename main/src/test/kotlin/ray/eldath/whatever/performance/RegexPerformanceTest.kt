package ray.eldath.whatever.performance

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import java.security.InvalidParameterException
import java.util.regex.Pattern
import kotlin.math.roundToInt
import kotlin.properties.Delegates
import kotlin.random.Random

@Deprecated("a too crude benchmark. will refactor to JMH in the future...")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object RegexPerformanceTest {
    private const val length = 10000000

    private val trueDataPool = listOf(
        "something.tobetest@gmail.com",
        "somethingttobetest@gamil.com",
        "so.methingtobetest@gamil.com"
    )
    private val falseDataPool = listOf(
        "some.ethingtobetestgamil.com",
        "some.hingtobetest@@gamil.com",
        "some.thingtobetest@gamilcomm"
    )
    private val trueData = arrayListOf<String>()
    private val falseData = arrayListOf<String>()

    private val noisePool = (' '..'?') + ('A'..'~')
    private val allCosts = arrayListOf<Long>()

    private fun evaluate(block: () -> Unit): Long {
        val start = System.currentTimeMillis()
        block()
        return System.currentTimeMillis() - start
    }

    private fun generateFakeData(datapool: List<String>, destination: MutableList<String>) {
        val actualLength = length - datapool[0].length
        if (actualLength < 0)
            throw InvalidParameterException("the target length can not hold even just the email")

        val random = Random.nextInt(0, length)
        val data = (1..actualLength).map { noisePool.random() }.joinToString("")
        val dataLength = data.length

        datapool.mapTo(destination) {
            data.substring(1 until random) + " $it " + data.substring(random + it.length until dataLength)
        }
    }

    private fun doEachTest(compiledRegex: Pattern, costs: MutableList<Long>, data: List<String>, expected: Boolean) {
        for (t in data) {
            var r = !expected
            val cost = evaluate { r = compiledRegex.matcher(t).find() }
            assertEquals(expected, r)
            allCosts.add(cost)
            costs.add(cost)
            println("\t$r checkpoint B: ${cost}ms")
        }
    }

    @BeforeEach
    fun generateData() {
        trueData.clear()
        falseData.clear()
        generateFakeData(
            trueDataPool,
            trueData
        )
        generateFakeData(
            falseDataPool,
            falseData
        )
    }

    @AfterAll
    fun computeOverallAverage() {
        println("\noverall cost average: ${allCosts.average().roundToInt()}ms")
    }

    @RepeatedTest(15)
    fun test() {
        val costs = arrayListOf<Long>()
        var regex by Delegates.notNull<Pattern>()
        val cost = evaluate {
            regex =
                Pattern.compile("[\\w!#\$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#\$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")
        }
        println("checkpoint A: ${cost}ms")
        doEachTest(regex, costs, trueData, true)
        doEachTest(regex, costs, falseData, false)
        println("partial cost average: ${costs.average().roundToInt()}ms\n")
    }
}