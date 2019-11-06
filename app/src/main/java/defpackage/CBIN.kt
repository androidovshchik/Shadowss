package defpackage

/**
 * Важны имена всего, тоже и в смежных классах
 */
@Suppress("FunctionName")
object CBIN {

    init {
        System.loadLibrary("cbin")
    }

    fun marshal(obj: Any): ByteArray? {
        return main_marshal(obj, obj.javaClass.name)
    }

    inline fun <reified T> unmarshal(bytes: ByteArray): Any? {
        return main_unmarshal(bytes, T::class.java.name)
    }

    private external fun main_marshal(obj: Any, className: String): ByteArray?

    external fun main_unmarshal(bytes: ByteArray, className: String): Any?
}