package ray.eldath.whatever

object CaptureReferenceBytecode {
    private var toBeCapture = 123

    val lambda = { toBeCapture += 2 }

    @JvmStatic
    fun main(args: Array<String>) {
        lambda()
        println(toBeCapture)
    }
}