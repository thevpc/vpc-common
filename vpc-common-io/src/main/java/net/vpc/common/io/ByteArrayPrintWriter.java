package net.vpc.common.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class ByteArrayPrintWriter extends PrintWriter {
    private ByteArrayOutputStream out;

    public ByteArrayPrintWriter() {
        this(new ByteArrayOutputStream());
    }

    public ByteArrayPrintWriter(ByteArrayOutputStream out) {
        super(out);
        this.out = out;

    }

    public byte[] toByteArray() {
        flush();
        return out.toByteArray();
    }

    public String toString() {
        flush();
        return out.toString();
    }

}
