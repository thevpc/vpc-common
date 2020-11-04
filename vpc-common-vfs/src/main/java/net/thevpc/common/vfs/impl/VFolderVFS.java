/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import net.thevpc.common.vfs.ListFS;
import net.thevpc.common.vfs.VFile;
import net.thevpc.common.vfs.VFileFilter;
import net.thevpc.common.vfs.VFileNameGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author taha.bensalah@gmail.com
 */
public class VFolderVFS extends AbstractDelegateVirtualFileSystem implements ListFS {

//    private LinkedHashMap<String, VFile> files = new LinkedHashMap<>();
    private VUserNode root = new VUserNode();

    public VFolderVFS(String id) {
        super(id);
        root.name = "";
        root.path = "/";
        root.parent = null;
    }

    @Override
    public String addOrRename(String name, VFile file, VFileNameGenerator nameGenerator) {
        if (name == null) {
            throw new NullPointerException("File Name is null");
        }
        if (file == null) {
            throw new NullPointerException("File is null");
        }
        List<String> pp = VFSUtils.toPathParts(name, true);
        if (pp.isEmpty()) {
            throw new NullPointerException("File Name is missing");
        }
        String sname = pp.get(pp.size() - 1);
        pp.remove(pp.size() - 1);
        VNode p = root.getNode(VFSUtils.concatPath(pp.toArray(new String[0])), true);
        if (!(p instanceof VUserNode)) {
            throw new NullPointerException("Invalid parent");
        }
        String validName = name;
        if (nameGenerator == null) {
            nameGenerator = DefaultFileNameGenerator.INSTANCE;
        }
        validName = nameGenerator.generateFileName(sname, (String t) -> p.getChild(t) != null);
        ((VUserNode) p).addChild(file, validName);
        return validName;
    }

    @Override
    public void add(String name, VFile file) {
        if (name == null) {
            throw new NullPointerException("File Name is null");
        }
        if (file == null) {
            throw new NullPointerException("File is null");
        }
        List<String> pp = VFSUtils.toPathParts(name, true);
        switch (pp.size()) {
            case 0: {
                throw new IllegalArgumentException("Missing name");
            }
            case 1: {
                root.addChild(name);
                break;
            }
            default: {
                pp.remove(pp.size() - 1);
                String parentPath = VFSUtils.concatPath(pp.toArray(new String[0]));
                VNode a = root.getNode(parentPath, true);
                if (a == null) {
                    throw new IllegalArgumentException("Parent not found " + parentPath);
                }
                if (a instanceof VUserNode) {
                    VUserNode vun = (VUserNode) a;
                    vun.addChild(file, name);
                } else {
                    throw new IllegalArgumentException("Parent not valid " + parentPath);
                }
                break;
            }

        }
    }

    @Override
    public void remove(String name) {
        if (!name.equals("/")) {
            VNode n = root.getNode(name, false);
            if (n != null && n instanceof VUserNode && n.parent != null) {
                ((VUserNode) n).remove(n.name);
                return;
            }
        }
        throw new NoSuchElementException(name + " not found");
    }

    @Override
    public VFile[] listFiles(final String path, final VFileFilter fileFilter) {
        VNode j = root.getNode(path, false);
        if (j == null) {
            return new VFile[0];
        }
        List<VFile> all = new ArrayList<>();
        for (VNode vNode : j.getChildren()) {
            VFile f = newFile(vNode.path);
            if (fileFilter == null || fileFilter.accept(f)) {
                all.add(f);
            }
        }
        return all.toArray(new VFile[0]);
    }

    public VFile getDelegate(String path) {
        VNode n = root.getNode(path, false);
        if (n == null || !(n instanceof VFileNode)) {
            return null;
        }
        VFileNode fn = (VFileNode) n;
        return fn.file;
    }

    @Override
    public String toString() {
        return "VFolderVFS{" + getId() + "}";
    }

    private static abstract class VNode {

        String name;
        String path;
        VNode parent;

        public abstract VNode[] getChildren();

        public abstract VNode getChild(String name);

        public abstract VNode addChild(String path);

        public VNode getNode(String path, boolean create) {
            List<String> a = VFSUtils.toPathParts(path, true);
            VNode n = this;
            for (String s : a) {
                VNode nn = n.getChild(s);
                if (nn == null) {
                    if (create) {
                        n = addChild(path);
                    }
                    if (nn == null) {
                        return null;
                    }
                }
            }
            return n;
        }
    }

    private static class VFileNode extends VNode {

        VFile file;
        boolean base;

        public VFileNode(VFile file, boolean base) {
            this.file = file;
            this.base = base;
        }

        @Override
        public VNode addChild(String path) {
            throw new UnsupportedOperationException("Not supported create sub file for " + path);
        }

        @Override
        public VNode[] getChildren() {
            List<VNode> r = new ArrayList<>();
            for (VFile listFile : file.listFiles()) {
                VNode c = getChild(listFile.getName());
                if (c != null) {
                    r.add(c);
                }
            }
            return r.toArray(new VNode[0]);
        }

        @Override
        public VNode getChild(String name) {
            if (file.isDirectory()) {
                VFile child = file.get(name);
                if (child != null) {
                    String p0 = file.getPath();
                    String p1 = child.getPath();
                    if (p1.startsWith(p0)) {
                        p1 = p1.substring(p0.length());
                        VFileNode c = new VFileNode(child, false);
                        c.name = name;
                        c.path = VFSUtils.concatPath(path, p1);
                        c.parent = this;
                        return c;
                    }
                }
            }
            return null;
        }

    }

    private static class VUserNode extends VNode {

        LinkedHashMap<String, VNode> children = new LinkedHashMap<String, VNode>();

        public VUserNode() {
        }

        @Override
        public VNode addChild(String name) {
            VUserNode g = new VUserNode();
            g.name = name;
            g.path = VFSUtils.concatPath(path, name);
            g.parent = this;
            children.put(name, g);
            return g;
        }

        @Override
        public VNode[] getChildren() {
            return children.values().toArray(new VNode[0]);
        }

        @Override
        public VNode getChild(String name) {
            return children.get(name);
        }

        public void addChild(VFile x, String name) {
            VFileNode g = new VFileNode(x, true);
            g.name = name;
            g.path = VFSUtils.concatPath(path, name);
            g.parent = this;
            children.put(name, g);
        }

        public boolean remove(String name) {
            return children.remove(name) != null;
        }

    }
}
