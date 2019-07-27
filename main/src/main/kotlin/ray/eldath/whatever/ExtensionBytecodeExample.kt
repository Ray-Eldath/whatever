package ray.eldath.whatever

fun List<Int>.printN(index: Int) {
    println(this[index])
}

val list = listOf(1, 3, 4).printN(2)