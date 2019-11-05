package defpackage.shadowss

/**
 * Важны имена пакета и класса
 */
@Suppress("FunctionName")
object CBIN {

    init {
        System.loadLibrary("bin")
    }

    private external fun encrypt(data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray

    private external fun decrypt(data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray
}