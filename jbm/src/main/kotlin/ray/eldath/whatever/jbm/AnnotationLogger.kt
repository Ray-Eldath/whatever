package ray.eldath.whatever.jbm

import javassist.ByteArrayClassPath
import javassist.ClassPool
import javassist.CtMethod
import javassist.bytecode.AnnotationsAttribute
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain

object MainAgent {
    @JvmStatic
    fun premain(agentArg: String?, instrumentation: Instrumentation) {
        instrumentation.addTransformer(AnnotationLoggerTransformer)
        println("AnnotationLoggerAgent now loaded.")
    }

    private object AnnotationLoggerTransformer : ClassFileTransformer {
        override fun transform(
            loader: ClassLoader?,
            className: String?,
            classBeingRedefined: Class<*>?,
            protectionDomain: ProtectionDomain?,
            classfileBuffer: ByteArray?
        ): ByteArray? {
            val pool = ClassPool.getDefault()
            pool.insertClassPath(ByteArrayClassPath(className, classfileBuffer))
            val clazz = pool[className?.replace("/", ".")]

            if (clazz.isFrozen) return null

            clazz.declaredMethods
                .filter { it.hasAnnotation("ray.eldath.whatever.jbm.LoggedMethod") }
                .forEach(::processMethod)

            return null
        }

        private fun processMethod(method: CtMethod) {
            println(method.methodInfo)
            val attribute =
                method.getAttribute(AnnotationsAttribute.invisibleTag) as AnnotationsAttribute
            println(attribute.name)
        }
    }
}