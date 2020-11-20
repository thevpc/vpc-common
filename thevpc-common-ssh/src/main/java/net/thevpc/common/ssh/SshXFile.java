package net.thevpc.common.ssh;

import net.thevpc.common.xfile.XFile;
import net.thevpc.common.xfile.XFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SshXFile extends XFile {
    private SshPath file;

    SshXFile(XFileSystem fs, String path, SshPath file) {
        super(fs, path);
        this.file = file;
    }


    private SshPath sshPath;
    private SshListener listener;

    public SshListener getListener() {
        return listener;
    }

    public SshXFile setListener(SshListener listener) {
        this.listener = listener;
        return this;
    }

    public String toString() {
        return toSshPath().toString();
    }

    public SshPath toSshPath() {
        return new SshPath(getPath());
    }

    public SshAddress toSshAddress() {
        return toSshPath().toAddress();
    }

    public InputStream getInputStream() {
        return new SshFileInputStream(sshPath);
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Unsupported protocol " + getProtocol());
    }

    public void rm(boolean recurse) {
        try (SShConnection session = new SShConnection(toSshAddress())
                .addListener(listener)
        ) {
            session.rm(toSshPath().getPath(), recurse);
        }
    }

    public void mkdir(boolean parents) {
        try (SShConnection c = new SShConnection(toSshAddress())
                .addListener(listener)
        ) {
            c.mkdir(toSshPath().getPath(), parents);
        }
    }

    public String getProtocol() {
        return "ssh";
    }

    public SshPath getSshPath() {
        return sshPath;
    }

    @Override
    public String getPath() {
        return sshPath.getPath();
    }
}
