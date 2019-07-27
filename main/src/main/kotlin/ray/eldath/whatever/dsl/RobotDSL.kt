package ray.eldath.whatever.dsl

class Direction(val name: String)

object Operates {
    val left = Direction("left")
    val right = Direction("right")
}

class Robot {
    infix fun turns(where: Direction): Unit = println("go ${where.name}")
}

fun operate(block: Operates.(Robot) -> Unit): Unit = Operates.block(
    Robot()
)

object RobotDSL {
    @JvmStatic
    fun main(args: Array<String>) {
        operate {
            it turns left
            it turns right
        }
    }
}
