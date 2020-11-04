/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import net.thevpc.common.vfs.VirtualFileACL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.thevpc.common.vfs.FileName;
import net.thevpc.common.vfs.VFile;
import net.thevpc.common.vfs.VFileFilter;
import net.thevpc.common.vfs.VFileType;
import net.thevpc.common.vfs.VFileVisitor;
import net.thevpc.common.vfs.VirtualFileSystem;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class DefaultFile implements VFile {

    private String name;
    private String path;
    private VirtualFileSystem fs;

    public DefaultFile(String path, VirtualFileSystem fs) {
        this.fs = fs;
        this.path = path;
        int index = path.lastIndexOf('/');
        if (index < 0) {
            name = path;
        } else {
            name = path.substring(index + 1);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getParentPath() {
        VFile p = getParentFile();
        return p == null ? null : p.getPath();
    }

    @Override
    public VFile getParentFile() {
        return fs.getParentFile(getPath());
    }

    @Override
    public boolean isFile() {
        return fs.isFile(getPath());
    }

    @Override
    public boolean isDirectory() {
        return fs.isDirectory(getPath());
    }

    @Override
    public VFileType getFileType() {
        return fs.getFileType(getPath());
    }

    @Override
    public VFile[] listFiles() {
        return fs.listFiles(getPath());
    }

    @Override
    public boolean mkdirs() {
        return fs.mkdirs(getPath());
    }

    @Override
    public void delete() throws IOException {
        fs.delete(getPath());
    }

    @Override
    public void deleteAll() throws IOException {
        fs.deleteAll(getPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return fs.getInputStream(getPath());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return fs.getOutputStream(getPath());
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException {
        return fs.getOutputStream(getPath(), append);
    }

    @Override
    public VFile[] listFiles(VFileFilter fileFilter) {
        return fs.listFiles(getPath(), fileFilter);
    }

    @Override
    public VFile get(String path) {
        return fs.get(getPath(), path);
    }

    @Override
    public long length() {
        return fs.length(getPath());
    }

    @Override
    public long lastModified() {
        return fs.lastModified(getPath());
    }

    @Override
    public VirtualFileSystem getFileSystem() {
        return fs;
    }

    @Override
    public boolean exists() {
        return fs.exists(getPath());
    }

    @Override
    public void renameTo(VFile file) throws IOException {
        fs.renameTo(getPath(), file);
    }

    @Override
    public void copyTo(VFile file) throws IOException {
        fs.copyTo(getPath(), file);
    }

    @Override
    public void copyTo(File file) throws IOException {
        fs.copyTo(getPath(), file);
    }

    @Override
    public void copyFrom(File file) throws IOException {
        fs.copyFrom(getPath(), file);
    }

    @Override
    public void copyTo(VFile outFile, VFileFilter filter) throws IOException {
        fs.copyTo(getPath(), outFile,filter);
    }

    @Override
    public boolean isParentOf(String path) {
        return fs.isParentOf(getPath(), path);
    }

    @Override
    public boolean isChildOf(String path) {
        return fs.isParentOf(path, getPath());
    }

    @Override
    public void visit(VFileVisitor visitor, VFileFilter filter) {
        VFSUtils.visit(this, visitor, filter);
    }

    public void visit(final String path, VFileVisitor visitor, VFileFilter filter) {
        VFSUtils.visit(this, path,visitor, filter);
    }

    @Override
    public VFile[] find(String path, VFileFilter filter) {
        final List<VFile> found=new ArrayList<>();
        visit(path, new VFileVisitor() {
            @Override
            public boolean visit(VFile pathname) {
                found.add(pathname);
                return true;
            }
        }, filter);
        return found.toArray(new VFile[found.size()]);
    }

    @Override
    public byte[] readBytes() throws IOException {
        return fs.readBytes(path);
    }

    @Override
    public void writeBytes(byte[] bytes) throws IOException {
        fs.writeBytes(path, bytes);
    }

    @Override
    public String probeContentType() throws IOException {
        return fs.probeContentType(path, false);
    }

    @Override
    public String probeContentType(boolean bestEffort) throws IOException {
        return fs.probeContentType(path, bestEffort);
    }

    @Override
    public String toString() {
        return "File{" + "path=" + path + ", fs=" + fs + '}';
    }

    @Override
    public FileName getFileName() {
        return VFSUtils.getFileName(getName());
    }

    @Override
    public VirtualFileACL getACL() {
        return getFileSystem().getACL(path);
    }

    @Override
    public VFile getBaseFile(String vfsId) {
        return fs.getBase(path,vfsId);
    }

    @Override
    public boolean isAllowedCreateChild(VFileType type, String user) {
        return getFileSystem().getSecurityManager().isAllowedCreateChild(getPath(),type, user);
    }

    @Override
    public boolean isAllowedRemoveChild(VFileType type, String user) {
        return getFileSystem().getSecurityManager().isAllowedRemoveChild(getPath(), type, user);
    }

    @Override
    public boolean isAllowedUpdateChild(VFileType type, String user) {
        return getFileSystem().getSecurityManager().isAllowedUpdateChild(getPath(), type, user);
    }

    @Override
    public boolean isAllowedList(String user) {
        return getFileSystem().getSecurityManager().isAllowedList(getPath(), user);
    }

    @Override
    public boolean isAllowedRemove(String user) {
        return getFileSystem().getSecurityManager().isAllowedRemove(getPath(), user);
    }

    @Override
    public boolean isAllowedRead(String user) {
        return getFileSystem().getSecurityManager().isAllowedRead(getPath(), user);
    }

    @Override
    public boolean isAllowedWrite(String user) {
        return getFileSystem().getSecurityManager().isAllowedWrite(getPath(),user);
    }

    @Override
    public File copyToNativeTempFile() throws IOException {
        return getFileSystem().copyToNativeTempFile(getPath());
    }
}
