package net.vpc.common.io;

import net.vpc.common.io.InputStreamSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class ByteArrayInputStreamSource implements InputStreamSource {
    private final byte[] bytes;

    public ByteArrayInputStreamSource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream open() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Object getSource() throws IOException {
        return bytes;
    }
}
