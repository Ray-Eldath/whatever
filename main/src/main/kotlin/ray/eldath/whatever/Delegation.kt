package ray.eldath.whatever

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

object Delegation {
    private val lazy by createDelegation {
        runBlocking {
            println("Haha! Blocked!")
            delay(3000L)
        }
        "Absalom! Absalom!"
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("I'M HERE!")
        println("Yes? $lazy")
    }
}

fun <T> createDelegation(type: Int = 0, initializer: () -> T): Proxy<T> =
    ProxyImpl(initializer)

sealed class Proxy<T> {
    abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
}

object DEFAULT

class ProxyImpl<T>(val initializer: () -> T) : Proxy<T>() {
    private var value = @Suppress("UNCHECKED_CAST") (DEFAULT as T)

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == DEFAULT)
            value = initializer()
        return value
    }
}