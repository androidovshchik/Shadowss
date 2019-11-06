package defpackage

import net.sourceforge.sizeof.SizeOf
import timber.log.Timber

/**
 * Важны имена всего, тоже и в смежных классах
 */
@Suppress("FunctionName")
object CBIN {

    private const val MIN_SIZE = 2 * 1024

    private const val MAX_SIZE = 16 * 1024 * 1024

    init {
        System.loadLibrary("cbin")
    }

    fun marshal(obj: Any): ByteArray? {
        var bytesSize = MIN_SIZE
        val objSize = SizeOf.deepSizeOf(obj)
        while (bytesSize < objSize) {
            bytesSize *= 2
        }
        if (bytesSize > MAX_SIZE) {
            Timber.e("marshal too much size ${SizeOf.humanReadable(objSize)}")
            return null
        }
        val bytes = ByteArray(bytesSize)
        return try {
            val result = main_marshal(obj, bytes)
            Timber.d("marshal $result")
            bytes
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
            val result = main_unmarshal(obj, bytes)
            Timber.d("unmarshal $result")
            obj
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    private external fun main_marshal(obj: Any, bytes: ByteArray): Int

    external fun main_unmarshal(obj: Any, bytes: ByteArray): Int
}