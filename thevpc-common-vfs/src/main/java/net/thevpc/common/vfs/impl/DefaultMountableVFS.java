/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.thevpc.common.vfs.MountableFS;
import net.thevpc.common.vfs.VFS;
import net.thevpc.common.vfs.VFile;
import net.thevpc.common.vfs.VFileFilter;
import net.thevpc.common.vfs.VirtualFileSystem;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class DefaultMountableVFS extends AbstractDelegateVirtualFileSystem implements MountableFS {

    private final List<MountAction> mountActions = new ArrayList<MountAction>();
    private final LinkedHashMap<String, VFile> files = new LinkedHashMap<String, VFile>();
    private RootNode root;

    public DefaultMountableVFS(String id) {
        super(id);
    }

    @Override
    public void mountEmptyFolder(String path) throws IOException {
        mount(path, VFS.createEmptyFS().get("/"), false);
    }

    @Override
    public void mount(String path, VFile fileOrFolder) throws IOException {
        mount(path, fileOrFolder, false);
    }

    @Override
    public void mount(String path, VFile fileOrFolder, boolean rename) throws IOException {
        VirtualFileSystem s = fileOrFolder.getFileSystem();
        if (s == this) {
            throw new IOException("Recursive Mounts are not allowed");
        }
        mountActions.add(new MountFolderAction(path, fileOrFolder, rename));
        invalidateRootNode();
    }

    @Override
    public VFile get(String path) {
        getRoot();
        return super.get(path);
    }

    @Override
    public boolean isDirectory(String path) {
        if (path.equals("/")) {
            return true;
        }
        Node n = root.get(path);
        if (n == null) {
            return false;
        }
        if (n instanceof ShadowNode) {
            return true;
        }
        VFile f = n.getFile();
        if (f == null) {
            return false;
        }
        return f.isDirectory();
    }

    @Override
    public VFile[] listFiles(String path, VFileFilter fileFilter) {
        path = normalizeVirtualPath(path);
        Node n = getRoot().get(path);
        if (n == null) {
            return new VFile[0];
        }
        return Arrays.stream(n.list()).map(x -> n.get(x))
                .filter(x -> x != null).map(x -> newFile(x.path))
                .toArray(VFile[]::new);
    }

    @Override
    public void umount(String path) throws IOException {
        path = normalizeVirtualPath(path);
        for (int i = 0; i < mountActions.size(); i++) {
            MountAction m = mountActions.get(i);
            if (m.getPath().equals(path)) {
                mountActions.remove(i);
                invalidateRootNode();
                return;
            }
        }
        throw new IllegalArgumentException("Mount point not found " + path);
    }

    private void invalidateRootNode() {
        this.root = null;
    }

    @Override
    public boolean exists(String path) {
        if ("/".equals(path)) {
            return true;
        }
        return super.exists(path);
    }

    @Override
    public VFile getDelegate(String path) {
        path = normalizeVirtualPath(path);
        Node n = getRoot().get(path);
        if (n == null) {
            return null;
        }
        return n.getFile();
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
    public VFile[] getRoots() {
        return new VFile[]{newFile("/")};
    }

    protected Node getRoot() {
        if (this.root != null) {
            return this.root;
        }
        RootNode _root = new RootNode();
        for (MountAction mount : mountActions) {
            String[] ss = VFSUtils.getParentAndName(mount.getPath());
            if (ss[0] == null) {
                if (mount instanceof MountFolderAction) {
                    _root.setMount((MountFolderAction) mount);
                } else {
                    //ignore
                }
            } else {
                ShadowNode pp = _root.getShadowOrCreate(ss[0]);
                pp.addChild(mount);
            }
        }
        return this.root = _root;
    }

    protected abstract static class Node {

        String name;
        String path;
        Node parent;

        public Node(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public abstract Node get(String path);

        public abstract VFile getFile();

        protected Node bindChild(Node n) {
            n.parent = this;
            return n;
        }

        public abstract String[] list();

        public String getPath() {
            return path;
        }

        @Override
        public String toString() {
            return getPath();
        }
    }

    protected static class RootNode extends Node {

        ShadowNode shadowNode = new ShadowNode("", "/");
        MountNode mountNode;

        public RootNode() {
            super("", "/");
        }

        @Override
        public VFile getFile() {
            if (mountNode != null) {
                return mountNode.getFile();
            }
            return null;
        }

        @Override
        public String[] list() {
            LinkedHashSet<String> a = new LinkedHashSet<>();
            a.addAll(Arrays.asList(shadowNode.list()));
            if (mountNode != null) {
                a.addAll(Arrays.asList(mountNode.list()));
            }
            return a.toArray(new String[0]);
        }

        public ShadowNode addChild(String name) {
            return shadowNode.addChild(name);
        }

        public Node setMount(MountFolderAction mp) {
            mountNode = new MountNode(mp);
            mountNode.name = mp.getPathParts().get(mp.getPathParts().size() - 1);
            return mountNode;
        }

        public ShadowNode getShadowOrCreate(String path) {
            if (path.isEmpty() || path.equals("/")) {
                return shadowNode;
            }
            return shadowNode.getShadowOrCreate(path);
        }

        public VFile getFile(String path) {
            Node n = get(path);
            if (n != null) {
                return n.getFile();
            }
            return null;
        }

        @Override
        public Node get(String path) {
            return mergeNodes(
                    shadowNode.get(path),
                    (mountNode == null) ? null : mountNode.get(path)
            );
        }
    }

    protected static Node mergeNodes(Node... all) {
        return mergeNodes(Arrays.asList(all));
    }

    protected static Node mergeNodes(List<Node> all) {
        List<Node> ok = all.stream().filter(x -> x != null).collect(Collectors.toList());
        if (ok.isEmpty()) {
            return null;
        }
        if (ok.size() == 1) {
            return ok.get(0);
        }
        return new MergedNode(ok.toArray(new Node[0]));
    }

    protected static class MergedNode extends Node {

        List<Node> children = new ArrayList<Node>();

        public MergedNode(Node... all) {
            super(all[0].name, all[0].path);
            children.addAll(Arrays.asList(all));
        }

        @Override
        public Node get(String path) {
            List<Node> tt = new ArrayList<>();
            for (Node node : children) {
                Node a = node.get(path);
                if (a != null) {
                    tt.add(a);
                }
            }
            return mergeNodes(tt);
        }

        @Override
        public VFile getFile() {
            for (Node node : children) {
                VFile f = node.getFile();
                if (f != null) {
                    return f;
                }
            }
            return null;
        }

        @Override
        public String[] list() {
            LinkedHashSet<String> tt = new LinkedHashSet<>();
            for (Node node : children) {
                tt.addAll(Arrays.asList(node.list()));
            }
            return tt.toArray(new String[0]);
        }

    }

    protected static class ShadowNode extends Node {

        Map<String, ShadowNode> shadows = new LinkedHashMap<String, ShadowNode>();
        Map<String, MountNode> mounts = new LinkedHashMap<String, MountNode>();

        public ShadowNode(String name, String path) {
            super(name, path);
        }

        public ShadowNode addChild(String name) {
            ShadowNode s = new ShadowNode(name, VFSUtils.concatPath(path, name));
            bindChild(s);
            shadows.put(s.name, s);
            return s;
        }

        @Override
        public String[] list() {
            LinkedHashSet<String> a = new LinkedHashSet<>();
            a.addAll(shadows.keySet());
            a.addAll(mounts.keySet());
            return a.toArray(new String[0]);
        }

        public Node addChild(MountAction action) {
            if (action instanceof MountFolderAction) {
                MountFolderAction mp = (MountFolderAction) action;
                if (mp.isRename()) {
                    String[] pathParts = VFSUtils.getParentAndName(mp.getPath());
                    String sparent = pathParts[0];
                    String sname = pathParts[1];
                    sname = DefaultFileNameGenerator.INSTANCE.generateFileName(sname, x -> mounts.containsKey(x));
                    mp = new MountFolderAction(VFSUtils.concatPath(sparent, sname), mp.getFile(), false);
                }
                MountNode s = new MountNode(mp);
                bindChild(s);
                mounts.put(s.name, s);

                //now should reset shadow
                if (s.getFile().isDirectory()) {
                    shadows.remove(s.name);
                }
                return s;
            } else {
                throw new IllegalArgumentException("Not yet Supported");
            }
        }

        public ShadowNode getShadowOrCreate(String path) {
            List<String> pp = VFSUtils.toPathParts(path, true);
            if (pp.isEmpty()) {
                return null;
            }
            String next = pp.remove(0);
            ShadowNode l = shadows.get(next);
            if (l == null) {
                l = addChild(next);
            }
            if (pp.isEmpty()) {
                return l;
            }
            return l.getShadowOrCreate(VFSUtils.toPath(pp));
        }

        @Override
        public Node get(String path) {
            List<String> pp = VFSUtils.toPathParts(path, true);
            if (pp.isEmpty()) {
                return this;
            }
            String next = pp.remove(0);
            ShadowNode sn = shadows.get(next);
            MountNode mn = mounts.get(next);
            return mergeNodes(
                    sn==null?null:sn.get(VFSUtils.toPath(pp)),
                    mn==null?null:mn.get(VFSUtils.toPath(pp))
            );
        }

        @Override
        public VFile getFile() {
            return null;
        }

    }

    protected static class MountNode extends Node {

        MountFolderAction mountPoint;

        public MountNode(MountFolderAction mountPoint) {
            super(
                    mountPoint.getPathParts().get(mountPoint.getPathParts().size() - 1),
                    mountPoint.getPath()
            );
            this.mountPoint = mountPoint;
        }

        @Override
        public Node get(String path) {
            List<String> t = VFSUtils.toPathParts(path, true);
            if (t.isEmpty()) {
                return this;
            }
            String s = t.remove(0);
            VFile l = this.mountPoint.getFile().get(s);
            if (l == null) {
                return null;
            }
            Node n = bindChild(new FileNode(l, VFSUtils.concatPath(this.path, s)));
            return n.get(VFSUtils.toPath(t));
        }

        @Override
        public VFile getFile() {
            return mountPoint.getFile();
        }

        @Override
        public String[] list() {
            return Arrays.stream(this.getFile().listFiles())
                    .map(VFile::getName).toArray(String[]::new);
        }

    }

    protected static class FileNode extends Node {

        private VFile file;

        public FileNode(VFile file, String path) {
            super(VFSUtils.getPathName(path), path);
            this.file = file;
            this.name = file.getName();
        }

        @Override
        public Node get(String name) {
            VFile l = file.get(name);
            if (l == null) {
                return null;
            }
            return bindChild(new FileNode(l, VFSUtils.concatPath(path, name)));
        }

        @Override
        public VFile getFile() {
            return file;
        }

        @Override
        public String[] list() {
            return Arrays.stream(this.file.listFiles())
                    .map(VFile::getName).toArray(String[]::new);
        }
    }

    @Override
    public String toString() {
        return "MountableFS{" + mountActions + '}';
    }

    public static class MountAction {

        private String path;
        private boolean rename;

        public MountAction(String path, boolean rename) {
            this.path = normalizeVirtualPath(path);
            this.rename = rename;
        }

        public String getPath() {
            return path;
        }

        public boolean isRename() {
            return rename;
        }

    }

    public static class MountFolderAction extends MountAction {

        private List<String> pathParts;
        private VFile file;

        public MountFolderAction(String path, VFile folder, boolean rename) {
            super(path, rename);
            this.file = folder;
            this.pathParts = VFSUtils.toPathParts(this.getPath(), true);
        }

        public VFile getFile() {
            return file;
        }

        public List<String> getPathParts() {
            return pathParts;
        }

        @Override
        public String toString() {
            return "MountPoint{" + getPath() + "=>" + file + '}';
        }
    }

}
