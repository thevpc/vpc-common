package net.thevpc.common.vfs.impl;

import net.thevpc.common.vfs.VFile;

/**
 * Created by vpc on 1/1/17.
 */
public interface VFileDelegate {
    VFile getDelegate(String path) ;
}
