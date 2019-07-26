package ray.eldath.whatever

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

// 结论：方法句柄垃圾。有空再调这破代码......
object ReflectAndMethodHandle {
    class ReflectAndMethodHandleTestClass {
        fun eval(x: Int) = x * 2

        private fun eval(x: Double): Double = x / 2

        fun lookup(): MethodHandles.Lookup = MethodHandles.lookup()
    }

    @Test
    fun testReflect() {
        val instance = ReflectAndMethodHandleTestClass()
        val clazz = instance.javaClass
        val eval = clazz.getMethod("eval", Int::class.java)
        assertEquals(eval(instance, 4), 8)

        val evalFloat = clazz.getDeclaredMethod("eval", Double::class.java)
        evalFloat.trySetAccessible()
        assertEquals(2.5, evalFloat(instance, 5.0))
    }

    @Test
    fun testMethodHandle() {
        val instance = ReflectAndMethodHandleTestClass()
        val clazz = instance.javaClass
        val methodType = MethodType.methodType(Double::class.java, Double::class.java)
        val method = MethodHandles
            .privateLookupIn(clazz, instance.lookup())
            .findVirtual(clazz, "eval", methodType)
        assertEquals(
            2.5,
            method.invoke(5.0)
        )
    }
}