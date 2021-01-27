/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import java.util.ArrayList;
import net.thevpc.common.vfs.VFSSecurityManager;
import net.thevpc.common.vfs.VFile;
import net.thevpc.common.vfs.VFileFilter;
import net.thevpc.common.vfs.VirtualFileSystem;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class FilteredFileSystem extends AbstractDelegateVirtualFileSystem {

    private final VirtualFileSystem fs;
    private final VFileFilter filter;

    public FilteredFileSystem(String id, VirtualFileSystem fs, VFileFilter filter) {
        super(id);
        if (filter == null) {
            throw new NullPointerException("Filter could not be null");
        }
        this.fs = fs;
        this.filter = filter;
    }

    @Override
    public VFSSecurityManager getSecurityManager() {
        return fs.getSecurityManager();
    }

    @Override
    public VFile get(String path) {
        VFile o1 = super.get(path);
        if (o1 != null && filter.accept(o1)) {
            return newFile(path);
        }
        return o1;
    }

    @Override
    public VFile getDelegate(String f) {
        VFile ff = fs.get(f);
        if (ff != null && filter.accept(ff)) {
            return newFile(ff.getPath());
        }
        return null;
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
        return filter(fs.listFiles(path));
    }

    @Override
    public VFile[] getRoots() {
        VFile[] r = filter(fs.getRoots());
        if (r.length > 0) {
            return r;
        }
        return new VFile[]{newFile("/")};
    }

    private VFile[] filter(VFile[] o) {
        ArrayList<VFile> r = new ArrayList<>();
        for (VFile o1 : o) {
            if (filter.accept(o1)) {
                r.add(newFile(o1.getPath()));
            }
        }
        return r.toArray(new VFile[0]);
    }

    @Override
    public String toString() {
        return "FilteredFS{" + "fs=" + fs + ", filter=" + filter + '}';
    }

}
