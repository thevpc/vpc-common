package net.thevpc.common.xfile;

import java.io.*;
import java.nio.file.Files;

public class JavaXFile extends XFile{
    private File file;
    JavaXFile(XFileSystem fs, String path, File file) {
        super(fs, path);
        this.file=file;
    }

    public String toString() {
        return file.getPath();
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public void rm(boolean recurse) throws IOException {
        File from1 = ((File) file);
        if (from1.isFile()) {
            try {
                Files.delete(from1.toPath());
            } catch (IOException e) {
                throw new IOException(e);
            }
        } else if (from1.isDirectory()) {
            if (recurse) {
                for (File file : from1.listFiles()) {
                    XFileSystems.get(file.getPath()).rm(recurse);
                }
            }
            try {
                Files.delete(from1.toPath());
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
    }

    @Override
    public void mkdir(boolean parents) {
        File from1 = file;
        if (parents) {
            from1.mkdirs();
        } else {
            from1.mkdir();
        }
    }

    @Override
    public String getProtocol() {
        return "file";
    }

    public File getFile() {
        return file;
    }
}
