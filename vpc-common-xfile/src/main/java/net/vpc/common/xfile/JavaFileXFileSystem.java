package net.vpc.common.xfile;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class JavaFileXFileSystem implements XFileSystem {

    @Override
    public int accept(String path) {
        if (path.startsWith("file:")) {
            return 1;
        } else if (!path.contains("://")) {
            return 1;
        }
        return -1;
    }

    @Override
    public XFile get(String path) {
        if (path.startsWith("file:")) {
            try {
                return new JavaXFile(this, path, Paths.get(new URL(path).toURI()).toFile());
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex.toString());
            }
        } else if (!path.contains("://")) {
            try {
                return new JavaXFile(this, path, new File(path));
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex.toString());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }
}
