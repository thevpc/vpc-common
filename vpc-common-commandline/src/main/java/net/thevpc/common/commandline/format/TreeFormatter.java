package net.thevpc.common.commandline.format;


import net.thevpc.common.commandline.Argument;
import net.thevpc.common.commandline.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class TreeFormatter<T> {

    public static final TreeLinkFormatter LINK_ASCII_FORMATTER = new AsciiTreeLinkFormatter();
    public static final TreeLinkFormatter LINK_SPACE_FORMATTER = new SpaceTreeLinkFormatter();

//    public static void main(String[] args) {
//        String s = new TreeFormatter(new TreeModel<File>() {
//            @Override
//            public File getRoot() {
////                return new File("/home/vpc/.nuts/default-workspace/");
//                return new File("/home/vpc/.nuts/default-workspace/programs/net/thevpc/nuts/");
//            }
//
//            @Override
//            public List<File> getChildren(File o) {
//                File f = (File) o;
//                File[] b = f.listFiles();
//                if (b == null) {
//                    return Collections.emptyList();
//                }
//                return Arrays.asList(b);
//            }
//        }).toString();
//        System.out.println(s);
//    }

    public static final TreeNodeFormatter TO_STRING_FORMATTER = new TreeNodeFormatter() {
        @Override
        public String format(Object o) {
            return String.valueOf(o);
        }
    };
    private TreeNodeFormatter formatter = TO_STRING_FORMATTER;
    private TreeLinkFormatter linkFormatter = LINK_ASCII_FORMATTER;
    private TreeModel tree;

    public TreeFormatter(TreeModel<T> tree) {
        this(tree, null, null);
    }

    public TreeFormatter(TreeModel<T> tree, TreeNodeFormatter formatter, TreeLinkFormatter linkFormatter) {
        if (formatter == null) {
            formatter = TO_STRING_FORMATTER;
        }
        if (linkFormatter == null) {
            linkFormatter = LINK_ASCII_FORMATTER;
        }
        if (tree == null) {
            throw new NullPointerException("N?ull Tree");
        }
        this.formatter = formatter;
        this.linkFormatter = linkFormatter;
        this.tree = tree;
    }

    public TreeNodeFormatter getFormatter() {
        return formatter;
    }

    public TreeFormatter setFormatter(TreeNodeFormatter formatter) {
        if (formatter == null) {
            formatter = TO_STRING_FORMATTER;
        }
        this.formatter = formatter;
        return this;
    }

    public TreeLinkFormatter getLinkFormatter() {
        return linkFormatter;
    }

    public TreeFormatter setLinkFormatter(TreeLinkFormatter linkFormatter) {
        if (linkFormatter == null) {
            linkFormatter = LINK_ASCII_FORMATTER;
        }
        this.linkFormatter = linkFormatter;
        return this;
    }

    public TreeModel getTree() {
        return tree;
    }

    public TreeFormatter setTree(TreeModel tree) {
        this.tree = tree;
        return this;
    }

    public String toString() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(b);
        print("", Type.FIRST, tree.getRoot(), out);
        out.flush();
        return b.toString();
    }

    public void print(PrintStream out) {
        print("", Type.FIRST, tree.getRoot(), out);
        out.flush();
    }

    private void print(String prefix, Type type, Object o, PrintStream out) {
        out.print(prefix);
        out.print(linkFormatter.formatMain(type));
        out.print(formatter.format(o));
        out.print("\n");
        Iterator<Object> children = tree.getChildren(o).iterator();
        Object last = null;
        if (children.hasNext()) {
            last = children.next();
        }
        while (children.hasNext()) {
            Object c = last;
            last = children.next();
            print(prefix + linkFormatter.formatChild(type), Type.MIDDLE, c, out);
        }
        if (last != null) {
            print(prefix + linkFormatter.formatChild(type), Type.LAST, last, out);
        }
    }

    public enum Type {
        FIRST,
        MIDDLE,
        LAST
    }

    public boolean configure(CommandLine cmdLine) {
        Argument a;
        if ((a = cmdLine.readStringOption("--border")) != null) {
            switch (a.getValue()) {
                case "simple": {
                    setLinkFormatter(LINK_ASCII_FORMATTER);
                    break;
                }
                case "none": {
                    setLinkFormatter(LINK_SPACE_FORMATTER);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private static class AsciiTreeLinkFormatter implements TreeLinkFormatter {
        @Override
        public String formatMain(Type type) {
            switch (type) {
                case FIRST: {
                    return ("");
                }
                case MIDDLE: {
                    return ("├── ");
                }
                case LAST: {
                    return ("└── ");
                }
            }
            return "";
        }

        @Override
        public String formatChild(Type type) {
            String p = "";
            switch (type) {
                case FIRST: {
                    p = "";
                    break;
                }
                case MIDDLE: {
                    p = "│   ";
                    break;
                }
                case LAST: {
                    p = "    ";
                    break;
                }
            }
            return p;
        }
    }

    private static class SpaceTreeLinkFormatter implements TreeLinkFormatter {
        @Override
        public String formatMain(Type type) {
            switch (type) {
                case FIRST: {
                    return ("");
                }
                case MIDDLE: {
                    return ("   ");
                }
                case LAST: {
                    return ("   ");
                }
            }
            return "";
        }

        @Override
        public String formatChild(Type type) {
            String p = "";
            switch (type) {
                case FIRST: {
                    p = "";
                    break;
                }
                case MIDDLE: {
                    p = "   ";
                    break;
                }
                case LAST: {
                    p = "   ";
                    break;
                }
            }
            return p;
        }
    }
}
