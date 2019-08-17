package net.vpc.common.xfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class JavaURLXFile extends XFile{
    private URL url;
    JavaURLXFile(XFileSystem fs, String path, URL url) {
        super(fs, path);
        this.url=url;
    }

    public String toString() {
        return url.toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("write-file: Unsupported protocol " + getProtocol());
    }

    public void rm(boolean recurse) throws IOException {
        throw new IOException("rm: Unsupported protocol " + getProtocol());
    }

    public void mkdir(boolean parents) throws IOException{
        throw new IOException("mkdir: Unsupported protocols " + getProtocol());
    }

    public String getProtocol() {
        return "url";
    }

    public URL getURL() {
        return url;
    }

    @Override
    public String getPath() {
        return url.getFile();
    }
}
