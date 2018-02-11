package net.vpc.common.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class StringInputStreamSource implements InputStreamSource {
    private final String string;

    public StringInputStreamSource(String string) {
        this.string = string;
    }

    @Override
    public InputStream open() throws IOException {
        return new ByteArrayInputStream(string.getBytes());
    }

    @Override
    public Object getSource() throws IOException {
        return string;
    }
}
