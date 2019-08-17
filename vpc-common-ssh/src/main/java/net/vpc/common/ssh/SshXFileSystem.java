package net.vpc.common.ssh;

import net.vpc.common.xfile.XFile;
import net.vpc.common.xfile.XFileSystem;

public class SshXFileSystem implements XFileSystem {
    @Override
    public int accept(String path) {
        if (path.startsWith("ssh:")) {
            return 2;
        }
        return -1;
    }

    @Override
    public XFile get(String path) {
        try {
            return new SshXFile(this, path, new SshPath(path));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.toString());
        }
    }
}
