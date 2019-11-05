package defpackage.shadowss.jbit;


// Code generated by colf(1); DO NOT EDIT.


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.InputMismatchException;

import static java.lang.String.format;


/**
 * Data bean with built-in serialization support.
 *
 * @author generated by colf(1)
 * @see <a href="https://github.com/pascaldekloe/colfer">Colfer's home</a>
 */
@javax.annotation.Generated(value = "colf(1)", comments = "Colfer from schema file structdef.colf")
public class MSNG implements Serializable {

    /**
     * The upper limit for serial byte sizes.
     */
    public static int colferSizeMax = 16 * 1024 * 1024;


    public short x;


    /**
     * Default constructor
     */
    public MSNG() {
        init();
    }


    /**
     * Colfer zero values.
     */
    private void init() {
    }

    /**
     * {@link #reset(InputStream) Reusable} deserialization of Colfer streams.
     */
    public static class Unmarshaller {

        /**
         * The data source.
         */
        protected InputStream in;

        /**
         * The read buffer.
         */
        public byte[] buf;

        /**
         * The {@link #buf buffer}'s data start index, inclusive.
         */
        protected int offset;

        /**
         * The {@link #buf buffer}'s data end index, exclusive.
         */
        protected int i;


        /**
         * @param in  the data source or {@code null}.
         * @param buf the initial buffer or {@code null}.
         */
        public Unmarshaller(InputStream in, byte[] buf) {
            // TODO: better size estimation
            if (buf == null || buf.length == 0)
                buf = new byte[Math.min(MSNG.colferSizeMax, 2048)];
            this.buf = buf;
            reset(in);
        }

        /**
         * Reuses the marshaller.
         *
         * @param in the data source or {@code null}.
         * @throws IllegalStateException on pending data.
         */
        public void reset(InputStream in) {
            if (this.i != this.offset) throw new IllegalStateException("colfer: pending data");
            this.in = in;
            this.offset = 0;
            this.i = 0;
        }

        /**
         * Deserializes the following object.
         *
         * @return the result or {@code null} when EOF.
         * @throws IOException            from the input stream.
         * @throws SecurityException      on an upper limit breach defined by {@link #colferSizeMax}.
         * @throws InputMismatchException when the data does not match this object's schema.
         */
        public MSNG next() throws IOException {
            if (in == null) return null;

            while (true) {
                if (this.i > this.offset) {
                    try {
                        MSNG o = new MSNG();
                        this.offset = o.unmarshal(this.buf, this.offset, this.i);
                        return o;
                    } catch (BufferUnderflowException e) {
                    }
                }
                // not enough data

                if (this.i <= this.offset) {
                    this.offset = 0;
                    this.i = 0;
                } else if (i == buf.length) {
                    byte[] src = this.buf;
                    // TODO: better size estimation
                    if (offset == 0)
                        this.buf = new byte[Math.min(MSNG.colferSizeMax, this.buf.length * 4)];
                    System.arraycopy(src, this.offset, this.buf, 0, this.i - this.offset);
                    this.i -= this.offset;
                    this.offset = 0;
                }
                assert this.i < this.buf.length;

                int n = in.read(buf, i, buf.length - i);
                if (n < 0) {
                    if (this.i > this.offset)
                        throw new InputMismatchException("colfer: pending data with EOF");
                    return null;
                }
                assert n > 0;
                i += n;
            }
        }

    }


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
    public byte[] marshal(OutputStream out, byte[] buf) throws IOException {
        // TODO: better size estimation
        if (buf == null || buf.length == 0)
            buf = new byte[Math.min(MSNG.colferSizeMax, 2048)];

        while (true) {
            int i;
            try {
                i = marshal(buf, 0);
            } catch (BufferOverflowException e) {
                buf = new byte[Math.min(MSNG.colferSizeMax, buf.length * 4)];
                continue;
            }

            out.write(buf, 0, i);
            return buf;
        }
    }

    /**
     * Serializes the object.
     *
     * @param buf    the data destination.
     * @param offset the initial index for {@code buf}, inclusive.
     * @return the final index for {@code buf}, exclusive.
     * @throws BufferOverflowException when {@code buf} is too small.
     * @throws IllegalStateException   on an upper limit breach defined by {@link #colferSizeMax}.
     */
    public int marshal(byte[] buf, int offset) {
        int i = offset;

        try {
            if (this.x != 0) {
                short x = this.x;
                if ((x & (short) 0xff00) != 0) {
                    buf[i++] = (byte) 0;
                    buf[i++] = (byte) (x >>> 8);
                } else {
                    buf[i++] = (byte) (0 | 0x80);
                }
                buf[i++] = (byte) x;
            }

            buf[i++] = (byte) 0x7f;
            return i;
        } catch (ArrayIndexOutOfBoundsException e) {
            if (i - offset > MSNG.colferSizeMax)
                throw new IllegalStateException(format("colfer: main.MSNG exceeds %d bytes", MSNG.colferSizeMax));
            if (i > buf.length) throw new BufferOverflowException();
            throw e;
        }
    }

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
    public int unmarshal(byte[] buf, int offset) {
        return unmarshal(buf, offset, buf.length);
    }

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
    public int unmarshal(byte[] buf, int offset, int end) {
        if (end > buf.length) end = buf.length;
        int i = offset;

        try {
            byte header = buf[i++];

            if (header == (byte) 0) {
                this.x = (short) ((buf[i++] & 0xff) << 8 | (buf[i++] & 0xff));
                header = buf[i++];
            } else if (header == (byte) (0 | 0x80)) {
                this.x = (short) (buf[i++] & 0xff);
                header = buf[i++];
            }

            if (header != (byte) 0x7f)
                throw new InputMismatchException(format("colfer: unknown header at byte %d", i - 1));
        } finally {
            if (i > end && end - offset < MSNG.colferSizeMax) throw new BufferUnderflowException();
            if (i < 0 || i - offset > MSNG.colferSizeMax)
                throw new SecurityException(format("colfer: main.MSNG exceeds %d bytes", MSNG.colferSizeMax));
            if (i > end) throw new BufferUnderflowException();
        }

        return i;
    }

    // {@link Serializable} version number.
    private static final long serialVersionUID = 1L;

    // {@link Serializable} Colfer extension.
    private void writeObject(ObjectOutputStream out) throws IOException {
        // TODO: better size estimation
        byte[] buf = new byte[1024];
        int n;
        while (true) try {
            n = marshal(buf, 0);
            break;
        } catch (BufferUnderflowException e) {
            buf = new byte[4 * buf.length];
        }

        out.writeInt(n);
        out.write(buf, 0, n);
    }

    // {@link Serializable} Colfer extension.
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        init();

        int n = in.readInt();
        byte[] buf = new byte[n];
        in.readFully(buf);
        unmarshal(buf, 0);
    }

    // {@link Serializable} Colfer extension.
    private void readObjectNoData() throws ObjectStreamException {
        init();
    }

    /**
     * Gets main.MSNG.x.
     *
     * @return the value.
     */
    public short getX() {
        return this.x;
    }

    /**
     * Sets main.MSNG.x.
     *
     * @param value the replacement.
     */
    public void setX(short value) {
        this.x = value;
    }

    /**
     * Sets main.MSNG.x.
     *
     * @param value the replacement.
     * @return {link this}.
     */
    public MSNG withX(short value) {
        this.x = value;
        return this;
    }

    @Override
    public final int hashCode() {
        int h = 1;
        h = 31 * h + (this.x & 0xffff);
        return h;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof MSNG && equals((MSNG) o);
    }

    public final boolean equals(MSNG o) {
        if (o == null) return false;
        if (o == this) return true;
        return o.getClass() == MSNG.class
                && this.x == o.x;
    }

}
