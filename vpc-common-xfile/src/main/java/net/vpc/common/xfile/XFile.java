package net.vpc.common.xfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class XFile {
    public static XFile of(String path){
        return XFileSystems.get(path);
    }

    protected XFileSystem fs;
    protected String path;

    public XFile(XFileSystem fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

    public abstract void rm(boolean recurse) throws IOException;

    public abstract void mkdir(boolean parents)throws IOException;

    public abstract String getProtocol();

}
