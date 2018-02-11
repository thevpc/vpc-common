package net.vpc.common.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

class InputStreamStringIterator implements Iterator<String> {
    private BufferedReader r;
    private String line;

    public InputStreamStringIterator(InputStream is) {
        r = new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public boolean hasNext() {
        if (r == null) {
            return false;
        }
        try {
            line = r.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line != null;
    }

    @Override
    public String next() {
        return line;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void finalize() throws Throwable {
        if (r != null) {
            r.close();
            r = null;
        }
    }
}
