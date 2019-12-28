package ray.eldath.whatever

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

object Coroutine {
    @RepeatedTest(3)
    fun cancelTest() {
        time {
            val job = GlobalScope.launch {
                while (isActive)
                    Thread.sleep(1000000)
            }
            job.cancel()
        }
    }

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

            yieldAll(7..10)
            println("done")
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

    private fun time(block: () -> Unit) {
        val s = System.currentTimeMillis()
        block()
        println("time used: ${System.currentTimeMillis() - s}ms")
    }
}