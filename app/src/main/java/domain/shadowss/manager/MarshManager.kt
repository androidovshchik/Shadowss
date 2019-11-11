package domain.shadowss.manager

import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
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

    fun unmarshal(bytes: ByteArray): Any? {
        try {
            val name = String(bytes, 0, 4, Charsets.UTF_8)
            val cls = Class.forName("domain.shadowss.model.marsh.$name.Unmarshaller")
            val constructor = cls.getConstructor(InputStream::class.java, ByteArray::class.java)
            val instance = constructor.newInstance(ByteArrayInputStream(bytes), null)
            val unmarshal = cls.getMethod("next")
            return unmarshal.invoke(instance)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return null
    }

    override fun release(vararg args: Any?) {}
}
