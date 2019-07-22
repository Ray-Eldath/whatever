package ray.eldath.whatever.performance

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import java.security.InvalidParameterException
import java.util.regex.Pattern
import kotlin.math.roundToInt
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object RegexPerformanceTest {
    private const val length = 10000

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

    private val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val allCosts = arrayListOf<Long>()

    private fun generateFakeData(datapool: List<String>, destination: MutableList<String>) {
        val actualLength = length - datapool[0].length
        if (actualLength < 0)
            throw InvalidParameterException("the target length can not hold even just the email")

        val random = Random.nextInt(1, length - 1)
        val data = (1..actualLength).map { alphaNumeric.random() }.joinToString("")
        val dataLength = data.length

        datapool.mapTo(destination) {
            data.substring(1 until random) + " $it " + data.substring(random + it.length until dataLength)
        }
    }

    private fun doEachTest(compiledRegex: Pattern, costs: MutableList<Long>, data: List<String>, expected: Boolean) {
        for (t in data) {
            val b1 = System.currentTimeMillis()
            val r = compiledRegex.matcher(t).find()
            val b2 = System.currentTimeMillis()
            assertEquals(expected, r)
            val cost = b2 - b1
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

    @RepeatedTest(1)
    fun test() {
        val costs = arrayListOf<Long>()
        val a = System.currentTimeMillis()
        val regex =
            Pattern.compile("[\\w!#\$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#\$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")
        val a1 = System.currentTimeMillis()
        println("checkpoint A: ${a1 - a}ms")
        doEachTest(regex, costs, trueData, true)
        doEachTest(regex, costs, falseData, false)
        println("partial cost average: ${costs.average().roundToInt()}ms\n")
    }
}