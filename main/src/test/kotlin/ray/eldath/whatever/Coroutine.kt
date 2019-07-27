package ray.eldath.whatever

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

object Coroutine {
    @Test
    fun basicTest() {
        println((arrayOf(1, 2, 3, "a", "b", 'c')).javaClass)
        val sequence = sequence {
            yield(1)
            println("one")

            yield(2)
            println("two")

            yield(3)
            println("three")

            yield("4")
            println("done")

            yieldAll('a'..'z')
        }
        println(sequence.toList())

        for (e in sequence)
            println(e)
    }

    private var progress = 0
    @RepeatedTest(10)
    fun test() {
        val a = System.currentTimeMillis()
        val sequence = sequence {
            yieldAll(generateSequence(2) { it * 23 })
        }
        val a1 = System.currentTimeMillis()
        val result = sequence.take(progress++ * 15000).toList()
        val b = System.currentTimeMillis()
        println("checkpoint A: ${a1 - a}ms\tcheckpoint B: ${b - a1}ms")
    }
}