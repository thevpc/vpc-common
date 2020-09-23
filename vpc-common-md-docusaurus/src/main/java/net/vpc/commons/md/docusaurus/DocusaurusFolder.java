package net.vpc.commons.md.docusaurus;

import net.vpc.commons.ljson.LJSON;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DocusaurusFolder implements DocusaurusFileOrFolder {
    public static final NameResolver DEFAULT_NAME_RESOLVER = new NameResolver() {
        @Override
        public boolean accept(DocusaurusFileOrFolder item, String name) {
            return item.getShortId().equals(name);
        }
    };
    public static final Comparator<DocusaurusFileOrFolder> DFOF_COMPARATOR = new Comparator<DocusaurusFileOrFolder>() {
        @Override
        public int compare(DocusaurusFileOrFolder o1, DocusaurusFileOrFolder o2) {
            if (o1.isFolder() && o2.isFile()) {
                return -1;
            }
            if (o1.isFile() && o2.isFolder()) {
                return 1;
            }
            int x = Integer.compare(o1.getOrder(), o2.getOrder());
            if (x != 0) {
                return x;
            }
            x = o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            if (x != 0) {
                return x;
            }
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };
    private String longId;
    private String shortId;
    private String title;
    private int order;
    private LJSON config;
    private DocusaurusFileOrFolder[] children;

    public DocusaurusFolder(String longId, String title, int order, LJSON config, DocusaurusFileOrFolder[] children) {
        this.longId = longId;
        String[] r = DocusaurusUtils.getPathArray(longId);
        this.shortId = r.length == 0 ? "/" : r[r.length - 1];
        this.title = title;
        this.order = order;
        this.config = config;
        this.children = children;
        Arrays.sort(children, DFOF_COMPARATOR);
    }

    public static DocusaurusFileOrFolder ofFileOrFolder(Path path, Path root) {
        return ofFileOrFolder(path,root,-1);
    }

    public static DocusaurusFileOrFolder ofFileOrFolder(Path path, Path root, int maxDepth) {
        return Files.isDirectory(path) ? DocusaurusFolder.ofFolder(path, root, maxDepth) : DocusaurusFile.ofFile(path, root);
    }

    public static DocusaurusFolder ofFolder(Path path, Path root, int maxDepth) {
        if (Files.isDirectory(path) && path.equals(root)) {
            try {
                return DocusaurusFolder.ofRoot(
                        (maxDepth == 0) ? new DocusaurusFileOrFolder[0] : Files.list(path).map(p -> DocusaurusFolder.ofFileOrFolder(p, root, maxDepth < 0 ? -1 : maxDepth - 1))
                                .filter(Objects::nonNull)
                                .toArray(DocusaurusFileOrFolder[]::new)

                );
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        } else if (Files.isDirectory(path)) {
            String longId = path.subpath(root.getNameCount(), path.getNameCount()).toString();
            Path dfi = path.resolve(".docusaurus-folder-config.json");
            LJSON config = LJSON.NULL;
            if (Files.isRegularFile(dfi)) {
                try {
                    config = LJSON.of(new String(Files.readAllBytes(dfi)));
                } catch (IOException e) {
                    //ignore...
                }
            }
            try {
                Integer order = config.get("order").asInt();
                if (order == null) {
                    order = 0;
                } else if (order <= 0) {
                    throw new IllegalArgumentException("");
                }
                String title = config.get("title").asString();
                if (title == null) {
                    title = path.getFileName().toString();
                }
                return new DocusaurusFolder(
                        longId,
                        title,
                        order,
                        config,
                        (maxDepth == 0) ? new DocusaurusFileOrFolder[0] : Files.list(path).map(p -> DocusaurusFolder.ofFileOrFolder(p, root, maxDepth < 0 ? -1 : maxDepth - 1))
                                .filter(Objects::nonNull)
                                .toArray(DocusaurusFileOrFolder[]::new)
                );
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        } else {
            throw new IllegalArgumentException("Unexpected");
        }
    }

    public static DocusaurusFolder ofRoot(DocusaurusFileOrFolder... children) {
        return new DocusaurusFolder("/", "/", 0, LJSON.NULL, children);
    }

    public static DocusaurusFolder of(String longId, String title, int order, LJSON config, DocusaurusFileOrFolder... children) {
        return new DocusaurusFolder(longId, title, order, config, children);
    }

    @Override
    public String getShortId() {
        return shortId;
    }

    @Override
    public String getLongId() {
        return longId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    public String toJSON(int indent) {
        StringBuilder sb = new StringBuilder();
        char[] indentChars = DocusaurusUtils.indentChars(indent);
        sb.append(indentChars);
        sb.append("'").append(DocusaurusUtils.escapeString(getTitle() + "': ["));
        if (children.length == 0) {
            sb.append("]");
        } else {
            for (DocusaurusFileOrFolder child : children) {
                sb.append("\n").append(child.toJSON(indent + 1)).append(",");
            }
            sb.append("\n").append(indentChars).append("]");
        }
        return sb.toString();
    }

    public DocusaurusFileOrFolder getImmediate(String name, boolean required, NameResolver nameResolver) {
        DocusaurusFileOrFolder[] a = getAllImmediate(name, nameResolver);
        if (a.length == 0) {
            if (required) {
                throw new NoSuchElementException("path not found: " + DocusaurusUtils.concatPath(longId, name));
            }
            return null;
        }
        if (a.length > 1) {
            throw new IllegalArgumentException("Ambiguous " + DocusaurusUtils.concatPath(longId, name));
        }
        return a[0];
    }

    public DocusaurusFileOrFolder[] getAllImmediate(String name, NameResolver nameResolver) {
        Predicate<DocusaurusFileOrFolder> filter2 = nameResolver != null ? x -> nameResolver.accept(x, name) : (x -> DEFAULT_NAME_RESOLVER.accept(x, name));
        return children().filter(filter2).toArray(DocusaurusFileOrFolder[]::new);
    }

    public DocusaurusFile getPage(String path, boolean required, NameResolver nameResolver) {
        DocusaurusFileOrFolder t = get(path, required, nameResolver);
        if (t instanceof DocusaurusFile) {
            return (DocusaurusFile) t;
        }
        if (required) {
            throw new NoSuchElementException("Invalid page path at path: " + path);
        }
        return null;
    }

    public DocusaurusFileOrFolder get(String path, boolean required, NameResolver nameResolver) {
        path = path.trim();
        if (path.isEmpty()/* || path.startsWith("/")*/) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
        String[] pathArray = DocusaurusUtils.getPathArray(path);
        return get(pathArray, required, nameResolver);
    }

    public DocusaurusFileOrFolder get(String[] pathArray, boolean required, NameResolver nameResolver) {
        if (pathArray.length == 0) {
            throw new IllegalArgumentException("Invalid path: <empty>");
        }
        DocusaurusFileOrFolder immediate = getImmediate(pathArray[0], required, nameResolver);
        if (immediate == null) {
            return null;
        }
        if (pathArray.length > 1) {
            if (immediate instanceof DocusaurusFolder) {
                String[] nextArray = new String[pathArray.length - 1];
                System.arraycopy(pathArray, 1, nextArray, 0, nextArray.length);
                return ((DocusaurusFolder) immediate).get(nextArray, required, nameResolver);
            } else {
                if (required) {
                    throw new NoSuchElementException("path not found: " + DocusaurusUtils.concatPath(longId, String.join("/", pathArray)));
                }
                return null;
            }
        }
        return immediate;
    }

    public Stream<DocusaurusFileOrFolder> children() {
        return Arrays.stream(children);
    }

    public DocusaurusFileOrFolder[] getChildren() {
        return children;
    }

    public LJSON getConfig() {
        return config;
    }

    public String toString() {
        String s = longId;
        if (!s.startsWith("/")) {
            s = "/" + s;
        }
        if (!s.endsWith("/")) {
            s = s + "/";
        }
        return s;
    }
}
