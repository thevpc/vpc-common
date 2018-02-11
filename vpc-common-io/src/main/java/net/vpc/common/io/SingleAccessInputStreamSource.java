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
class SingleAccessInputStreamSource implements InputStreamSource {
    private InputStream file;

    public SingleAccessInputStreamSource(InputStream file) {
        this.file = file;
    }

    @Override
    public InputStream open() throws IOException {
        InputStream file=this.file;
        this.file=null;
        if (file != null) {
            return file;
        }
        throw new IOException("Invalid Stream");
    }

    @Override
    public Object getSource() throws IOException {
        return file;
    }
    
}
