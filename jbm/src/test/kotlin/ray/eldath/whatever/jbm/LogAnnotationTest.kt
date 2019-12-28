package ray.eldath.whatever.jbm

class LogAnnotationTest {
    @LoggedMethod
    fun hello(@LoggedParameter a: String, b: String) {
        println("Hello, $a. $b")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LogAnnotationTest().hello("Phosphorus", "It's nice to meet you.")
        }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class LoggedMethod

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class LoggedParameter