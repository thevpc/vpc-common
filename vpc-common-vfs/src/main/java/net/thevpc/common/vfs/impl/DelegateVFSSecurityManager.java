/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import java.util.function.Function;
import net.thevpc.common.vfs.VFSSecurityManager;
import net.thevpc.common.vfs.VFile;
import net.thevpc.common.vfs.VFileType;

/**
 *
 * @author thevpc
 */
public class DelegateVFSSecurityManager implements VFSSecurityManager {

    private Function<String, VFile> delegate;

    public DelegateVFSSecurityManager(Function<String, VFile> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isAllowedCreateChild(String path, VFileType type, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedCreateChild(type, user);
    }

    protected VFile getDelegateFile(String path) {
        return delegate.apply(path);
    }

    @Override
    public boolean isAllowedRemoveChild(String path, VFileType type, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedRemoveChild(type, user);
    }

    @Override
    public boolean isAllowedUpdateChild(String path, VFileType type, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedUpdateChild(type, user);
    }

    @Override
    public boolean isAllowedList(String path, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedList(user);
    }

    @Override
    public boolean isAllowedRemove(String path, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedRemove(user);
    }

    @Override
    public boolean isAllowedRead(String path, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedRead(user);
    }

    @Override
    public boolean isAllowedWrite(String path, String user) {
        VFile f = getDelegateFile(path);
        if (f == null) {
            return false;
        }
        return f.isAllowedWrite(user);
    }
}
