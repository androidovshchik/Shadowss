package defpackage

/**
 * Важны имена пакета и класса, тоже и в смежных классах
 */
@Suppress("FunctionName")
object CBIN {

    init {
        System.loadLibrary("cbin")
    }

    external fun main_marshal(obj: Any, className: String = obj.javaClass.name): ByteArray?

    external fun main_unmarshal(bytes: ByteArray, className: String): Any?
}