package domain.shadowss.model

import java.io.IOException
import java.io.OutputStream
import java.nio.BufferOverflowException
import java.nio.BufferUnderflowException
import java.util.*

interface Colfer {

    /**
     * Serializes the object.
     *
     * @param out the data destination.
     * @param buf the initial buffer or {@code null}.
     * @return the final buffer. When the serial fits into {@code buf} then the return is {@code buf}.
     * Otherwise the return is a new buffer, large enough to hold the whole serial.
     * @throws IOException           from {@code out}.
     * @throws IllegalStateException on an upper limit breach defined by {@link #colferSizeMax}.
     */
    @Throws(Throwable::class)
    fun marshal(out: OutputStream, buf: ByteArray?): ByteArray

    /**
     * Serializes the object.
     *
     * @param buf    the data destination.
     * @param offset the initial index for {@code buf}, inclusive.
     * @return the final index for {@code buf}, exclusive.
     * @throws BufferOverflowException when {@code buf} is too small.
     * @throws IllegalStateException   on an upper limit breach defined by {@link #colferSizeMax}.
     */
    @Throws(Throwable::class)
    fun marshal(buf: ByteArray, offset: Int): Int

    /**
     * Deserializes the object.
     *
     * @param buf    the data source.
     * @param offset the initial index for {@code buf}, inclusive.
     * @return the final index for {@code buf}, exclusive.
     * @throws BufferUnderflowException when {@code buf} is incomplete. (EOF)
     * @throws SecurityException        on an upper limit breach defined by {@link #colferSizeMax}.
     * @throws InputMismatchException   when the data does not match this object's schema.
     */
    @Throws(Throwable::class)
    fun unmarshal(buf: ByteArray, offset: Int): Int

    /**
     * Deserializes the object.
     *
     * @param buf    the data source.
     * @param offset the initial index for {@code buf}, inclusive.
     * @param end    the index limit for {@code buf}, exclusive.
     * @return the final index for {@code buf}, exclusive.
     * @throws BufferUnderflowException when {@code buf} is incomplete. (EOF)
     * @throws SecurityException        on an upper limit breach defined by {@link #colferSizeMax}.
     * @throws InputMismatchException   when the data does not match this object's schema.
     */
    @Throws(Throwable::class)
    fun unmarshal(buf: ByteArray, offset: Int, end: Int): Int
}