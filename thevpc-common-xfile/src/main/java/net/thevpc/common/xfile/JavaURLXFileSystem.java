package net.thevpc.common.xfile;

import java.net.URL;

public class JavaURLXFileSystem implements XFileSystem {

    @Override
    public int accept(String path) {
        if (path.startsWith("file:")) {
            return 0;
        } else if (!path.contains("://")) {
            return 1;
        }
        return -1;
    }

    @Override
    public XFile get(String path) {
        try {
            return new JavaURLXFile(this, path, new URL(path));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.toString());
        }
    }
}
