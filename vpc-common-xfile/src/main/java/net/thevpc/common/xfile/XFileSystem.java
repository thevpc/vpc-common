package net.thevpc.common.xfile;

public interface XFileSystem {
    int accept(String path);

    XFile get(String path);
}
