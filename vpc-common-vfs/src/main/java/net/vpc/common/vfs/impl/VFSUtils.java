package net.vpc.common.vfs.impl;

import net.vpc.common.vfs.*;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by vpc on 1/1/17.
 */
public class VFSUtils {

    public static void writeBytes(OutputStream o, byte[] bytes) throws IOException {
        try {
            o.write(bytes);
        } finally {
            if (o != null) {
                o.close();
            }
        }
    }

    public static byte[] readBytes(InputStream ios) throws IOException {
        ByteArrayOutputStream ous = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
            }

            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        if (ous == null) {
            throw new IOException();
        }
        return ous.toByteArray();
    }

    public static String getParentPath(String file) {
        if (file == null) {
            return null;
        }
        String path = file;
        List<String> r = VFSUtils.toPathParts(path, true);
        if (r.isEmpty()) {
            return null;
        }
        r.remove(r.size() - 1);
        if (r.isEmpty()) {
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        for (String n : r) {
            sb.append("/").append(n);
        }
        return sb.toString();
    }

    public static String concatPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            boolean endsWithSlash = sb.length() > 0 && sb.charAt(sb.length() - 1) == '/';
            if (path != null) {
                while (path.startsWith("/")) {
                    path = path.substring(1);
                }
                if (path.length() > 0) {
                    if (!endsWithSlash) {
                        sb.append("/");
                    }
                    sb.append(path);
                }
            }
        }
        return sb.toString();
    }

    public static String toPath(List<String> path) {
        if (path.isEmpty()) {
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : path) {
            sb.append("/").append(s);
        }
        return sb.toString();
    }

    public static String getPathParent(String path) {
        List<String> h = toPathParts(path, true);
        if (h.isEmpty() || h.size() == 1) {
            return null;
        }
        h.remove(h.size() - 1);
        return toPath(h);
    }

    public static String[] getParentAndName(String path) {
        List<String> h = toPathParts(path, true);
        if (h.isEmpty()) {
            return new String[]{null, ""};
        }
        String n = h.remove(h.size() - 1);
        return new String[]{
            toPath(h),
            n
        };

    }

    public static String getPathName(String path) {
        List<String> h = toPathParts(path, true);
        if (h.isEmpty()) {
            return null;
        }
        return h.get(h.size() - 1);
    }

    public static List<String> toPathParts(String path, boolean compact) {
        List<String> r = new ArrayList<String>();
        if (path == null) {
            return r;
        }
        for (String i : path.split("/|\\\\")) {
            if (i.length() > 0) {
                if (compact) {
                    if (i.equals(".")) {
                        //do nothing
                    } else if (i.equals("..")) {
                        if (r.size() > 0) {
                            r.remove(r.size() - 1);
                        }
                    } else {
                        r.add(i);
                    }
                } else {
                    r.add(i);
                }
            }
        }
        return r;
    }

    public static String wildcardToRegex(String pattern) {
        if (pattern == null) {
            pattern = "*";
        }
        int i = 0;
        char[] cc = pattern.toCharArray();
        StringBuilder sb = new StringBuilder("^");
        while (i < cc.length) {
            char c = cc[i];
            switch (c) {
                case '.':
                case '!':
                case '$':
                case '{':
                case '}':
                case '+':
                case '[':
                case ']': {
                    sb.append('\\').append(c);
                    break;
                }
                case '?': {
                    sb.append("[^/\\\\]");
                    break;
                }
                case '*': {
                    if (i + 1 < cc.length && cc[i + 1] == '*') {
                        i++;
                        sb.append(".*");
                    } else {
                        sb.append("[^/\\\\]*");
                    }
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
            i++;
        }
        sb.append('$');
        return sb.toString();
    }

    public static void visit(VFile inFile, VFileVisitor visitor, VFileFilter filter) {
        Stack<VFile> stack = new Stack();
        stack.push(inFile);
        boolean root = true;
        while (!stack.isEmpty()) {
            VFile x = stack.pop();
            boolean wasRoot = root;
            if (root) {
                root = false;
            }
            if (wasRoot || filter == null || filter.accept(x)) {
                if (x.isDirectory()) {
                    if (!visitor.visit(x)) {
                        return;
                    }
                    VFile[] sub = x.listFiles();
                    for (int i = sub.length - 1; i >= 0; i--) {
                        stack.push(sub[i]);
                    }
                } else if (!visitor.visit(x)) {
                    break;
                }
            }
        }
    }

    public static void visit(VFile inFile, final String path, VFileVisitor visitor, VFileFilter filter) {
        class Val {

            VFile file;
            String[] next;

            public Val(VFile file, String[] next) {
                this.file = file;
                this.next = next;
            }
        }

        Stack<Val> stack = new Stack();
        List<String> strings = VFSUtils.toPathParts(path, true);
        stack.push(new Val(
                inFile,
                strings.toArray(new String[strings.size()])
        ));
        boolean root = true;
        while (!stack.isEmpty()) {
            Val x = stack.pop();
            if (root) {
                root = false;
            }
            if (x.next.length == 0) {
                if (filter == null || filter.accept(x.file)) {
                    if (!visitor.visit(x.file)) {
                        break;
                    }
                }
            } else {
                if (x.file.isDirectory()) {
                    strings = new ArrayList<>(Arrays.asList(x.next));
                    String first = strings.remove(0);
                    String[] next = strings.toArray(new String[strings.size()]);
                    if (first.contains("**")) {
                        final Pattern pattern = Pattern.compile(VFSUtils.wildcardToRegex(first));
                        final String basePath = x.file.getPath();
                        x.file.visit(new VFileVisitor() {
                            @Override
                            public boolean visit(VFile pathname) {
//                            basePath.
                                String path2 = pathname.getPath().substring(basePath.length() + 1);
                                return pattern.matcher(path2).matches();
                            }
                        }, filter);
                    } else if (first.contains("*")) {
                        final Pattern pattern = Pattern.compile(VFSUtils.wildcardToRegex(first));
                        for (VFile child : x.file.listFiles(new VFileFilter() {
                            @Override
                            public boolean accept(VFile pathname) {
                                return pattern.matcher(pathname.getName()).matches();
                            }
                        })) {
                            stack.push(new Val(child, next));
                        }
                    } else {
                        VFile vFile = x.file.get(first);
                        if (vFile.exists()) {
                            stack.push(new Val(vFile, next));
                        }
                    }
                }
            }
        }
    }

    public static void copy(VFile inFile, VFile outFile) throws IOException {
        copy(inFile, outFile, null);
    }
    public static void move(VFile inFile, VFile outFile) throws IOException {
        move(inFile, outFile, null);
    }

    public static void copy(VFile inFile, VFile outFile, VFileFilter filter) throws IOException {
        if (filter == null) {
            VFile inFileBase = inFile.getBaseFile(VFS.NATIVE_FS.getId());
            VFile outFileBase = outFile.getBaseFile(VFS.NATIVE_FS.getId());
            if (inFileBase != null && outFileBase != null) {
                String src = inFileBase.getPath();
                String dest = outFileBase.getPath();
                Path srcPath = Paths.get(src);
                boolean overwrite = true;
                if (Files.isDirectory(srcPath)) {
                    try {
                        Files.walk(srcPath).forEach(a -> {
                            Path b = Paths.get(dest, a.toString().substring(src.length()));
                            try {
                                if (!a.toString().equals(src)) {
                                    Files.copy(a, b, overwrite ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
                                }
                            } catch (IOException ex) {
                                throw new UncheckedIOException(ex);
                            }
                        });
                    } catch (UncheckedIOException ex) {
                        throw ex.getCause();
                    }
                } else {
                    InputStream fin = null;
                    OutputStream fout = null;
                    try {
                        fin = inFileBase.getInputStream();
                        fout = outFileBase.getOutputStream(false);
                        copy(fin, fout, 2048);
                    } finally {
                        if (fin != null) {
                            fin.close();
                        }
                        if (fout != null) {
                            fout.close();
                        }
                    }
                }
                return;
            }
        }
        class InOut {

            VFile inFile;
            VFile outFile;

            public InOut(VFile inFile, VFile outFile) {
                this.inFile = inFile;
                this.outFile = outFile;
            }
        }
        Stack<InOut> stack = new Stack();
        stack.push(new InOut(inFile, outFile));
        boolean root = true;
        while (!stack.isEmpty()) {
            InOut x = stack.pop();
            boolean wasRoot = root;
            if (root) {
                root = false;
            }
            if (wasRoot || filter == null || filter.accept(x.inFile)) {
                if (x.inFile.isDirectory()) {
                    if (x.outFile.exists()) {
                        //do nothing
                    } else {
                        x.outFile.mkdirs();
                    }
                    VFile[] sub = x.inFile.listFiles();
                    for (int i = sub.length - 1; i >= 0; i--) {
                        stack.push(new InOut(sub[i], x.outFile.get(sub[i].getName())));
                    }
                } else {
                    VFile f_out = x.outFile;
                    if (x.outFile.exists() && x.outFile.isDirectory()) {
                        f_out = x.outFile.get(inFile.getName());
                    }

                    if (x.inFile.exists()) {
                        if (f_out.getParentFile() != null && !f_out.getParentFile().exists()) {
                            if (!f_out.getParentFile().mkdirs()) {
                                throw new IOException("Unable to create folder " + f_out.getParentFile());
                            }
                        }
                        InputStream ins = null;
                        OutputStream outs = null;
                        try {
                            ins = x.inFile.getInputStream();
                            try {
                                outs = f_out.getOutputStream();

                                copy(ins, outs, Math.max(1014 * 1024, (int) x.inFile.length()));

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

    public static void move(VFile inFile, VFile outFile, VFileFilter filter) throws IOException {
        if (filter == null) {
            VFile inFileBase = inFile.getBaseFile(VFS.NATIVE_FS.getId());
            VFile outFileBase = outFile.getBaseFile(VFS.NATIVE_FS.getId());
            if (inFileBase != null && outFileBase != null) {
                String src = inFileBase.getPath();
                String dest = outFileBase.getPath();
                Path srcPath = Paths.get(src);
                Path destPath = Paths.get(dest);
                boolean overwrite = true;
                CopyOption[] options = overwrite ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
                if (Files.isDirectory(srcPath)) {
                    try {
                        Files.walk(srcPath).forEach(a -> {
                            Path b = Paths.get(dest, a.toString().substring(src.length()));
                            try {
                                if (!a.toString().equals(src)) {
                                    Files.move(a, b, options);
                                }
                            } catch (IOException ex) {
                                throw new UncheckedIOException(ex);
                            }
                        });
                    } catch (UncheckedIOException ex) {
                        throw ex.getCause();
                    }
                } else {
                    Files.move(srcPath, destPath, options);
                }
                return;
            }
        }
        class InOut {

            VFile inFile;
            VFile outFile;

            public InOut(VFile inFile, VFile outFile) {
                this.inFile = inFile;
                this.outFile = outFile;
            }
        }
        Stack<InOut> stack = new Stack();
        stack.push(new InOut(inFile, outFile));
        boolean root = true;
        while (!stack.isEmpty()) {
            InOut x = stack.pop();
            if (x.outFile == null) {
                x.inFile.delete();
                continue;
            }
            boolean wasRoot = root;
            if (root) {
                root = false;
            }
            if (wasRoot || filter == null || filter.accept(x.inFile)) {
                if (x.inFile.isDirectory()) {
                    if (x.outFile.exists()) {
                        //do nothing
                    } else {
                        x.outFile.mkdirs();
                    }
                    VFile[] sub = x.inFile.listFiles();
                    if (sub.length > 0) {
                        //push frist this to be deleted after all children moved!
                        stack.push(new InOut(x.inFile, null));
                        for (int i = sub.length - 1; i >= 0; i--) {
                            stack.push(new InOut(sub[i], x.outFile.get(sub[i].getName())));
                        }
                    }
                } else {
                    VFile f_out = x.outFile;
                    if (x.outFile.exists() && x.outFile.isDirectory()) {
                        f_out = x.outFile.get(inFile.getName());
                    }

                    if (x.inFile.exists()) {
                        if (f_out.getParentFile() != null && !f_out.getParentFile().exists()) {
                            if (!f_out.getParentFile().mkdirs()) {
                                throw new IOException("Unable to create folder " + f_out.getParentFile());
                            }
                        }
                        InputStream ins = null;
                        OutputStream outs = null;
                        try {
                            ins = x.inFile.getInputStream();
                            try {
                                outs = f_out.getOutputStream();

                                copy(ins, outs, Math.max(1014 * 1024, (int) x.inFile.length()));

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
                        x.inFile.delete();
                    }

//                    copyFiles(x.in, f_out);
                }
            }
        }
    }

    public static void copy(File inFile, VFile outFile, FileFilter filter) throws IOException {
        class InOut {

            File inFile;
            VFile outFile;

            public InOut(File inFile, VFile outFile) {
                this.inFile = inFile;
                this.outFile = outFile;
            }
        }
        Stack<InOut> stack = new Stack();
        stack.push(new InOut(inFile, outFile));
        boolean root = true;
        while (!stack.isEmpty()) {
            InOut x = stack.pop();
            boolean wasRoot = root;
            if (root) {
                root = false;
            }
            if (wasRoot || filter == null || filter.accept(x.inFile)) {
                if (x.inFile.isDirectory()) {
                    if (x.outFile.exists()) {
                        //do nothing
                    } else {
                        x.outFile.mkdirs();
                    }
                    File[] sub = x.inFile.listFiles();
                    for (int i = sub.length - 1; i >= 0; i--) {
                        stack.push(new InOut(sub[i], x.outFile.get(sub[i].getName())));
                    }
                } else {
                    VFile f_out = x.outFile;
                    if (x.outFile.exists() && x.outFile.isDirectory()) {
                        f_out = x.outFile.get(inFile.getName());
                    }

                    if (x.inFile.exists()) {
                        if (f_out.getParentFile() != null && !f_out.getParentFile().exists()) {
                            if (!f_out.getParentFile().mkdirs()) {
                                throw new IOException("Unable to create folder " + f_out.getParentFile());
                            }
                        }
                        InputStream ins = null;
                        OutputStream outs = null;
                        try {
                            ins = new FileInputStream(x.inFile);
                            try {
                                outs = f_out.getOutputStream();

                                copy(ins, outs, Math.max(1014 * 1024, (int) x.inFile.length()));

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

    public static void copy(InputStream inStream, OutputStream outStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int r = -1;
        while ((r = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, r);
        }
    }

    public static void copy(File inFile, VFile outFile) throws IOException {
        InputStream fin = null;
        OutputStream fout = null;
        try {
            fin = new FileInputStream(inFile);
            fout = outFile.getOutputStream(false);
            copy(fin, fout, 2048);
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    public static void copy(VFile inFile, File outFile) throws IOException {
        InputStream fin = null;
        OutputStream fout = null;
        try {
            fin = inFile.getInputStream();
            fout = new FileOutputStream(outFile, false);
            copy(fin, fout, 2048);
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    public static File copyNativeTempFile(VFile inFile) throws IOException {
        File f = File.createTempFile("tmp_", inFile.getName());
        VFSUtils.copy(inFile, VFS.NATIVE_FS.get(f.getPath()));
        return f;
    }

    public static boolean isParentOf(String parent, String child) {
        List<String> p = VFSUtils.toPathParts(parent, true);
        List<String> c = VFSUtils.toPathParts(child, true);
        if (c.size() > p.size()) {
            for (int i = 0; i < p.size(); i++) {
                if (!p.get(i).equals(c.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String[] splitName(String baseName, boolean longExtension) {
        if (baseName != null) {
            int dot = longExtension ? baseName.indexOf('.') : baseName.lastIndexOf('.');
            if (dot < 0) {
                return new String[]{baseName, null};
            } else if (dot == 0) {
                return new String[]{"", baseName};
            } else if (dot == baseName.length() - 1) {
                return new String[]{baseName, ""};
            } else {
                return new String[]{
                    baseName.substring(0, dot),
                    baseName.substring(dot + 1)
                };
            }
        }
        return new String[]{null, null};
    }

    public static FileName getFileName(String name) {
        String[] s1 = splitName(name, true);
        String[] s2 = splitName(name, false);
        return new FileName(s1[0], s1[1], s2[0], s2[1]);
    }

}
