/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author taha.bensalah@gmail.com
 */
class URLInputStreamSource implements InputStreamSource {
    private final URL file;

    public URLInputStreamSource(URL file) {
        this.file = file;
    }

    @Override
    public InputStream open() throws IOException {
        InputStream s = file.openStream();
        if (s != null) {
            return s;
        }
        throw new IOException("Invalid URL " + file);
    }

    @Override
    public Object getSource() throws IOException {
        return file;
    }
    
}
