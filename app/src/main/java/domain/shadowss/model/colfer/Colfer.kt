package domain.shadowss.model.colfer

import java.io.OutputStream

interface Colfer {

    @Throws(Throwable::class)
    fun marshal(out: OutputStream, buf: ByteArray): ByteArray

    @Throws(Throwable::class)
    fun marshal(buf: ByteArray, offset: Int): Int

    @Throws(Throwable::class)
    fun unmarshal(buf: ByteArray, offset: Int): Int

    @Throws(Throwable::class)
    fun unmarshal(buf: ByteArray, offset: Int, end: Int): Int
}