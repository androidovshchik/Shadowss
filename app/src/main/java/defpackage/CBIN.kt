package defpackage

/**
 * Важны имена пакета и класса
 */
@Suppress("FunctionName")
object CBIN {

    init {
        System.loadLibrary("cbin")
    }

    external fun main_marshal_len(o: Any): Int

    external fun main_marshal(o: Any, void* buf): Int

    external fun main_unmarshal(o: Any, const void* data ,  datalen): Int
}