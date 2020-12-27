/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.thevpc.common.vfs.FileName;
import net.thevpc.common.vfs.VFile;
import net.thevpc.common.vfs.VFileFilter;
import net.thevpc.common.vfs.VFileType;
import net.thevpc.common.vfs.VFileVisitor;
import net.thevpc.common.vfs.VirtualFileACL;
import net.thevpc.common.vfs.VirtualFileSystem;

/**
 *
 * @author thevpc
 */
public class DetachedFile implements VFile {

    private String name;
    private String path;
    private boolean exists = true;
    private long length = 0;
    private long lastModified = 0;
    private VirtualFileACL acl;
    private Supplier<InputStream> inputSupplier;
    private Function<Boolean, OutputStream> outputSupplier;
    private VFileType fileType = VFileType.FILE;

    public DetachedFile(String path, VirtualFileSystem fs) {
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
        return null;
    }

    @Override
    public boolean isFile() {
        return fileType == VFileType.FILE;
    }

    @Override
    public boolean isDirectory() {
        return fileType == VFileType.DIRECTORY;
    }

    @Override
    public VFileType getFileType() {
        return fileType;
    }

    @Override
    public VFile[] listFiles() {
        return new VFile[0];
    }

    @Override
    public boolean mkdirs() {
        return false;
    }

    @Override
    public void delete() throws IOException {
    }

    @Override
    public void deleteAll() throws IOException {
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream s = inputSupplier == null ? null : inputSupplier.get();
        if (s == null) {
            throw new IOException("missing InputStream");
        }
        return s;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        OutputStream s = outputSupplier == null ? null : outputSupplier.apply(false);
        if (s == null) {
            throw new IOException("missing OutputStream");
        }
        return s;
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException {
        OutputStream s = outputSupplier == null ? null : outputSupplier.apply(true);
        if (s == null) {
            throw new IOException("missing OutputStream");
        }
        return s;
    }

    @Override
    public VFile[] listFiles(VFileFilter fileFilter) {
        return new VFile[0];
    }

    @Override
    public VFile get(String path) {
        return null;
    }

    @Override
    public long length() {
        return length;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public VirtualFileSystem getFileSystem() {
        return DetachedVirtualFileSystem.INSTANCE;
    }

    @Override
    public boolean exists() {
        return exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    @Override
    public void renameTo(VFile file) throws IOException {
        throw new IllegalArgumentException("Unsupported rename");
    }

    @Override
    public void copyTo(VFile file) throws IOException {
        VFSUtils.copy(this, file);
    }

    @Override
    public void copyTo(File file) throws IOException {
        VFSUtils.copy(this, file);
    }

    @Override
    public void copyFrom(File file) throws IOException {
        VFSUtils.copy(file, this);
    }

    @Override
    public void copyTo(VFile outFile, VFileFilter filter) throws IOException {
        VFSUtils.copy(this, outFile, filter);
    }

    @Override
    public boolean isParentOf(String path) {
        return VFSUtils.isParentOf(getPath(), path);
    }

    @Override
    public boolean isChildOf(String path) {
        return VFSUtils.isParentOf(path, getPath());
    }

    @Override
    public void visit(VFileVisitor visitor, VFileFilter filter) {
        VFSUtils.visit(this, visitor, filter);
    }

    public void visit(final String path, VFileVisitor visitor, VFileFilter filter) {
        VFSUtils.visit(this, path, visitor, filter);
    }

    @Override
    public VFile[] find(String path, VFileFilter filter) {
        final List<VFile> found = new ArrayList<>();
        visit(path, (VFile pathname) -> {
            found.add(pathname);
            return true;
        }, filter);
        return found.toArray(new VFile[found.size()]);
    }

    @Override
    public byte[] readBytes() throws IOException {
        try (InputStream in = getInputStream()) {
            return VFSUtils.readBytes(in);
        }
    }

    @Override
    public void writeBytes(byte[] bytes) throws IOException {
        try (OutputStream out = getOutputStream()) {
            out.write(bytes);
        }
    }

    @Override
    public String probeContentType() throws IOException {
        return ContentTypeUtils.probeContentType(getPath());
    }

    @Override
    public String probeContentType(boolean bestEffort) throws IOException {
        return ContentTypeUtils.probeContentType(getPath());
    }

    @Override
    public String toString() {
        return "DetachedFile{" + "path=" + path + '}';
    }

    @Override
    public FileName getFileName() {
        return VFSUtils.getFileName(getName());
    }

    @Override
    public VirtualFileACL getACL() {
        return acl;
    }

    @Override
    public VFile getBaseFile(String vfsId) {
        if (vfsId == null || vfsId.length() == 0 || vfsId.equalsIgnoreCase(DetachedVirtualFileSystem.INSTANCE.getId())) {
            return this;
        }
        return null;
    }

    @Override
    public boolean isAllowedCreateChild(VFileType type, String user) {
        return true;
    }

    @Override
    public boolean isAllowedRemoveChild(VFileType type, String user) {
        return true;
    }

    @Override
    public boolean isAllowedUpdateChild(VFileType type, String user) {
        return true;
    }

    @Override
    public boolean isAllowedList(String user) {
        return true;
    }

    @Override
    public boolean isAllowedRemove(String user) {
        return true;
    }

    @Override
    public boolean isAllowedRead(String user) {
        return true;
    }

    @Override
    public boolean isAllowedWrite(String user) {
        return true;
    }

    @Override
    public File copyToNativeTempFile() throws IOException {
        File f = File.createTempFile("tmp_", getName());
        try (InputStream in = getInputStream()) {
            try (OutputStream out = new FileOutputStream(f)) {
                VFSUtils.copy(getInputStream(), out, 8092);
            }
        }
        return f;
    }

    public Supplier<InputStream> getInputSupplier() {
        return inputSupplier;
    }

    public void setInputSupplier(Supplier<InputStream> inputSupplier) {
        this.inputSupplier = inputSupplier;
    }

    public Function<Boolean, OutputStream> getOutputSupplier() {
        return outputSupplier;
    }

    public void setOutputSupplier(Function<Boolean, OutputStream> outputSupplier) {
        this.outputSupplier = outputSupplier;
    }

    public void setFileType(VFileType fileType) {
        this.fileType = fileType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAcl(VirtualFileACL acl) {
        this.acl = acl;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

}
