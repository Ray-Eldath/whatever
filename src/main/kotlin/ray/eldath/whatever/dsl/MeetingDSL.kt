package ray.eldath.whatever.dsl

class Timestamp {
    private var time = IntRange(0, 0)

    override fun toString(): String = "${time.first}:${time.last}"

    infix fun at(range: IntRange) {
        time = range
    }
}

class Meeting(private val name: String) {
    val start = Timestamp()
    val end = Timestamp()
    var theme = ""

    override fun toString(): String = "the meeting $name:$theme will start at $start and end at $end"
}

infix fun String.meeting(block: Meeting.() -> Unit): Meeting = Meeting(
    this
).apply(block)

class MeetingDSL {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            "planning" meeting {
                start at 2..10
                end at 7..20
            }
            listOf("a", "B").map { it.toUpperCase() }.let { it.sumBy { a -> a.length } }
        }
    }
}

class User(var height: Double) {
    // custom getter:
    val isTall
        get() = height > 180.0

    fun isTallI() = height > 180.0

    val list = listOf("3", "4", "5").asSequence()
        .map { it.toInt() }
        .filter { it > 4 }
        .count()
}