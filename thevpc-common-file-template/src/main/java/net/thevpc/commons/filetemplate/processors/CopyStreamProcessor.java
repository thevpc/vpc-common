package net.thevpc.commons.filetemplate.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import net.thevpc.commons.filetemplate.FileTemplater;
import net.thevpc.commons.filetemplate.StreamProcessor;

public class CopyStreamProcessor implements StreamProcessor {

    @Override
    public void processStream(InputStream source, OutputStream target, FileTemplater context) {
        try {
            byte[] buffer = new byte[1024];
            int r;
            while ((r = source.read(buffer)) > 0) {
                target.write(buffer, 0, r);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public String toString() {
        return "Copy";
    }
    

}
