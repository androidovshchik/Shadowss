package domain.shadowss.manager

import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.OutputStream

@Suppress("MemberVisibilityCanBePrivate")
class MarshManager : Manager {

    override fun init(vararg args: Any?) {
    }

    fun marshal(instance: Any): ByteArray {
        val output = ByteArrayOutputStream()
        try {
            val cls = instance.javaClass
            output.write(cls.simpleName.toByteArray())
            val marshal = cls.getMethod("marshal", OutputStream::class.java, ByteArray::class.java)
            marshal.invoke(instance, output, null)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return output.toByteArray()
    }

    override fun release(vararg args: Any?) {}
}
