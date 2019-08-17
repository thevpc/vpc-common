package net.vpc.common.io;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;

public class SingleTarGzOutputStream extends OutputStream {
    private OutputStream output;
    private File tempFile;
    private OutputStream tempStream;
    private String name;

    public SingleTarGzOutputStream(File file) throws IOException {
        this(file,null,null);
    }

    public SingleTarGzOutputStream(File file,File tempFile,String name) throws IOException {
        this(new FileOutputStream(file),
                tempFile==null?new File(file.getPath()+".~"):tempFile,
                (name==null||name.trim().isEmpty()) ? (file.getName().toLowerCase().endsWith(".tar.gz") ? file.getName().substring(0,file.getName().length()-".tar.gz".length()): file.getName()) : name);
    }

    public SingleTarGzOutputStream(OutputStream output, File tempFile, String name) throws IOException {
        this.output = output;
        this.tempFile = tempFile;
        this.name = name;
        this.tempStream = new FileOutputStream(tempFile);
    }

    @Override
    public void write(int b) throws IOException {
        tempStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        tempStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        tempStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        tempStream.flush();
    }

    @Override
    public void close() throws IOException {
        tempStream.close();
        TarArchiveOutputStream tos;
        TarArchiveEntry entry;
        tos = new TarArchiveOutputStream(output);
        tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
        entry = new TarArchiveEntry(tempFile,name);
        tos.putArchiveEntry(entry);
        FileInputStream in = new FileInputStream(tempFile);
        byte buf[] = new byte[8192*8];
        int read;
        while ((read = in.read(buf)) > 0)
            tos.write(buf, 0, read);
        in.close();
        tos.closeArchiveEntry();
        tos.close();
        output.close();
        tempFile.delete();
    }
}
