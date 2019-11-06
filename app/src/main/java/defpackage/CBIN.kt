package defpackage

import timber.log.Timber

/**
 * Важны имена всего, тоже и в смежных классах
 */
@Suppress("FunctionName")
object CBIN {

    init {
        System.loadLibrary("cbin")
    }

    fun marshal(obj: Any): ByteArray? {
        val bytes = ByteArray()
        try {
            val result = main_marshal(obj, bytes)
            Timber.d("marshal $result")
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return bytes
    }

    inline fun <reified T : Any> unmarshal(bytes: ByteArray): Any {
        val obj = T::class.java.getConstructor(T::class.java)
            .newInstance()
        try {
            val result = main_unmarshal(obj, bytes)
            Timber.d("unmarshal $result")
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return obj
    }

    private external fun main_marshal(obj: Any, bytes: ByteArray): Int

    external fun main_unmarshal(obj: Any, bytes: ByteArray): Int
}