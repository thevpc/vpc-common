/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class FileUtils {

    public static final class ExtentionFilterOptions {

        private boolean ignoreCase = true;
        private boolean alwaysAcceptFolders = true;

        public ExtentionFilterOptions() {
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public ExtentionFilterOptions setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            return this;
        }

        public boolean isAlwaysAcceptFolders() {
            return alwaysAcceptFolders;
        }

        public ExtentionFilterOptions setAlwaysAcceptFolders(boolean alwaysAcceptFolders) {
            this.alwaysAcceptFolders = alwaysAcceptFolders;
            return this;
        }

    }

    public static FileFilter createExtensionFilter(final ExtentionFilterOptions o, String... extensions) throws IOException {
        return new ExtensionFileFilterImpl(o, extensions);

    }

    public static void copy(File in, File out) throws IOException {
        copy(in, out, null);
    }

    public static void copy(File in, File out, FileFilter filter) throws IOException {
        class InOut {

            File in;
            File out;

            public InOut(File in, File out) {
                this.in = in;
                this.out = out;
            }
        }
        Stack<InOut> stack = new Stack();
        stack.push(new InOut(in, out));
        boolean root = true;
        while (!stack.isEmpty()) {
            InOut x = stack.pop();
            boolean wasRoot = root;
            if (root) {
                root = false;
            }
            if (wasRoot || filter == null || filter.accept(x.in)) {
                if (x.in.isDirectory()) {
                    if (x.out.exists()) {
                        //do nothing
                    } else {
                        x.out.mkdirs();
                    }
                    File[] sub = x.in.listFiles();
                    for (int i = sub.length - 1; i >= 0; i--) {
                        stack.push(new InOut(sub[i], new File(x.out, sub[i].getName())));
                    }
                } else {
                    File f_out = x.out;
                    if (x.out.exists() && x.out.isDirectory()) {
                        f_out = new File(x.out, in.getName());
                    }

                    if (x.in.exists()) {
                        if (f_out.getParentFile() != null && !f_out.getParentFile().exists()) {
                            if (!f_out.getParentFile().mkdirs()) {
                                throw new IOException("Unable to create folder " + f_out.getParent());
                            }
                        }
                        InputStream ins = null;
                        OutputStream outs = null;
                        try {
                            ins = new FileInputStream(x.in);
                            try {
                                outs = new FileOutputStream(f_out);

                                copy(ins, outs, Math.max(1014 * 1024, (int) x.in.length()));

                            } finally {
                                if (outs != null) {
                                    outs.close();
                                }
                            }
                        } finally {
                            if (ins != null) {
                                ins.close();
                            }
                        }
                    }

//                    copyFiles(x.in, f_out);
                }
            }
        }
    }

//    private static void copyFiles(File f_in, File f_out) throws IOException {
//
//    }
    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, 4 * 1024);
    }

    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int r;
        while ((r = in.read(buffer)) > 0) {
            out.write(buffer, 0, r);
        }
    }

    private static class ExtensionFileFilterImpl implements FileFilter {

        private final ExtentionFilterOptions o;
        private final String[] extensions;

        public ExtensionFileFilterImpl(ExtentionFilterOptions o, String[] extensions) {
            this.o = o == null ? new ExtentionFilterOptions() : o;
            this.extensions = extensions;
        }

        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            }
            if (!pathname.isFile()) {
                return false;
            }
            for (String extension : extensions) {
                if (acceptFileByExt(pathname, extension)) {
                    return true;
                }
            }
            return false;
        }

        public boolean acceptFileByExt(File pathname, String extension) {
            if (o.ignoreCase) {
                return pathname.getName().toLowerCase().endsWith("." + extension.toLowerCase());
            } else {
                return pathname.getName().endsWith("." + extension);
            }
        }
    }

    public static File changeFileSuffix(File file, String suffix){
        PathInfo p = PathInfo.create(file);
        return new File(
                PathInfo.formatPath(p.getDirName(),p.getNamePart()+suffix,p.getExtensionPart())
        );
    }
    public static File changeFileExtension(File file, String suffix){
        PathInfo p = PathInfo.create(file);
        return new File(
                PathInfo.formatPath(p.getDirName(),p.getNamePart(),suffix)
        );
    }
}
