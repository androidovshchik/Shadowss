package defpackage

import org.ehcache.sizeof.SizeOf
import timber.log.Timber

/**
 * Важны имена всего, тоже и в смежных классах
 */
@Suppress("FunctionName")
object CBIN {

    private const val MIN_SIZE = 2 * 1024

    private const val MAX_SIZE = 16 * 1024 * 1024

    private val sizeOf = SizeOf.newInstance()

    init {
        System.loadLibrary("cbin")
    }

    fun marshal(obj: Any): ByteArray? {
        var bytesSize = MIN_SIZE
        val objSize = sizeOf.deepSizeOf(obj)
        while (bytesSize < objSize) {
            bytesSize *= 2
        }
        if (bytesSize > MAX_SIZE) {
            Timber.w("marshal too much size $objSize")
            return null
        }
        val bytes = ByteArray(bytesSize)
        return try {
            if (main_marshal(obj, obj.javaClass.name, bytes)) {
                bytes
            } else {
                null
            }
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    // todo also detect class by 4 bytes
    inline fun <reified T : Any> unmarshal(bytes: ByteArray): Any? {
        val cls = T::class.java
        val obj = cls.getConstructor(cls)
            .newInstance()
        return try {
            if (main_unmarshal(obj, cls.name, bytes)) {
                obj
            } else {
                null
            }
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    private external fun main_marshal(obj: Any, className: String, bytes: ByteArray): Boolean

    external fun main_unmarshal(obj: Any, className: String, bytes: ByteArray): Boolean
}