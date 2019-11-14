/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.vfs;

import java.io.IOException;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface MountableFS extends VirtualFileSystem {

    /**
     * mount folder (from another File System)
     *
     * @param path path to mount to
     * @param folder for to mount
     * @throws IOException error when
     */
    public void mount(String path, VFile folder) throws IOException;

    /**
     * mount folder (from another File System)
     *
     * @param path path to mount to
     * @param folder for to mount
     * @param rename if a file with the same file exists rename it
     * @throws IOException error when
     */
    public void mount(String path, VFile folder, boolean rename) throws IOException;

    public void umount(String path) throws IOException;

    void mountEmptyFolder(String path) throws IOException;
}
