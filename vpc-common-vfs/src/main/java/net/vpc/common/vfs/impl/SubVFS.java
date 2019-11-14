/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.vfs.impl;

import java.util.ArrayList;
import java.util.List;
import net.vpc.common.vfs.VFile;
import net.vpc.common.vfs.VFileFilter;
import net.vpc.common.vfs.VirtualFileSystem;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class SubVFS extends AbstractDelegateVirtualFileSystem {

    private final String prefix;
    private final VirtualFileSystem fs;
//    private final List<String> prefixParts;
    private final int prefixPartsCount;

    public SubVFS(String id, VirtualFileSystem fs, String prefix) {
        super(id);
        this.fs = fs;
        this.prefix = (prefix == null || prefix.trim().isEmpty()) ? null : normalizeVirtualPath(prefix);
//        this.prefixParts = VFSUtils.toPathParts(this.prefix, true);
        this.prefixPartsCount = VFSUtils.toPathParts(this.prefix, true).size();
    }

    @Override
    public VFile getDelegate(String f) {
        if (f.equals("/")) {
            return fs.get(prefix);
        } else {
            return fs.get(prefix, normalizeVirtualPath(f));
        }
    }

    @Override
    public VFile getBase(String path, String vfsId) {
        if (vfsId == null || vfsId.length() == 0 || vfsId.equalsIgnoreCase(getId())) {
            return get(path);
        }
        VFile t = getDelegate(path);
        if (t != null) {
            return t.getBaseFile(vfsId);
        }
        return null;
    }

    @Override
    public VFile[] listFiles(String path, VFileFilter fileFilter) {
        VFile f = getDelegate(path);
        if (f == null) {
            return new VFile[0];
        }
        VFile[] d = f.getFileSystem().listFiles(f.getPath());
        ArrayList<VFile> r = new ArrayList<>();
        for (VFile d1 : d) {
            List<String> pp = VFSUtils.toPathParts(d1.getPath(), true);
            for (int i = 0; i < prefixPartsCount; i++) {
                pp.remove(0);
            }
            VFile ff = get(toPathString(pp));
            if (fileFilter == null || fileFilter.accept(ff)) {
                r.add(ff);
            }
        }
        return r.toArray(new VFile[r.size()]);
    }

    @Override
    public VFile[] getRoots() {
        return new VFile[]{newFile("/")};
    }

    @Override
    public String toString() {
        return "SubFS{" + prefix + " @ " + fs + '}';
    }

}
