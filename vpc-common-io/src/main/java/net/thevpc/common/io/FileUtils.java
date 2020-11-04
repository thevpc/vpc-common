/**
 * ====================================================================
 *            vpc-common-io : common reusable library for
 *                          input/output
 *
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
 *
 * Copyright (C) 2016-2020 thevpc
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <br>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <br>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.io;

//import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class FileUtils {

    public static boolean deleteFolderTree(File folder, FileFilter fileFilter) {
        if (!folder.exists()) {
            return true;
        }
        File[] files = folder.listFiles(fileFilter);
        boolean ok = true;
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
                    if (!file.delete()) {
                        ok = false;
                    }
                } else if (file.isDirectory()) {
                    deleteFolderTree(file, fileFilter);
                } else {
                    ok = false;
                }
            }
        }
        return ok && folder.delete();
    }

    public static String[] findFilePathsOrError(String path, File cwd, FileFilter fileFilter) {
        File[] files = findFilesOrError(path, cwd, fileFilter);
        String[] strings = new String[files.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = files[i].getPath();
        }
        return strings;
    }

    public static File[] findFilesOrError(String path, File cwd, FileFilter fileFilter) {
        File[] all = findFiles(path, cwd, fileFilter);
        if (all.length == 0) {
            throw new IllegalArgumentException("No file found " + path);
        }
        return all;
    }

    public static File[] findFiles(String path, File cwd, FileFilter fileFilter) {
        File f = getAbsoluteFile(cwd, path);
        if (f.isAbsolute()) {
            File f0 = f;
            while (f0.getParentFile() != null && f0.getParentFile().getParent() != null) {
                f0 = f0.getParentFile();
            }
            return findFiles(f.getPath().substring(f0.getParent().length()), f0.getParent(), cwd, fileFilter);
        } else {
            return findFiles(path, ".", cwd, fileFilter);
        }
    }

    public static File[] findFiles(String path, String base, File cwd, final FileFilter fileFilter) {
        int x = path.indexOf('/');
        if (x > 0) {
            String parent = path.substring(0, x);
            String child = path.substring(x + 1);
            List<File> all = new ArrayList<>();
            for (File file : findFiles(parent, base, cwd, fileFilter)) {
                Collections.addAll(all, findFiles(child, file.getPath(), cwd, fileFilter));
            }
            return all.toArray(new File[all.size()]);
        } else {
            if (path.contains("*") || path.contains("?")) {
                final Pattern s = Pattern.compile(PrivateStringUtils.simpexpToRegexp(path));
                File[] files = getAbsoluteFile(cwd, base).listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return (fileFilter==null || fileFilter.accept(pathname))
                                &&
                                s.matcher(pathname.getName()).matches();
                    }
                });
                if (files == null) {
                    return new File[0];
                }
                return files;
            } else {
                File f = new File(getAbsolutePath(base), path);
                if (f.exists()) {
                    return new File[]{f};
                }
                return new File[0];
            }
        }
    }

    public static String getCanonicalPath(File fileName) {
        try {
            return fileName.getCanonicalPath();
        } catch (IOException e) {
            return fileName.getAbsolutePath();
        }
    }

    public static String probeContentType(String fileName) {
        try {
            return Files.probeContentType(Paths.get(fileName));
        } catch (IOException ex) {
            //ignore
        }
        return null;
        // NO MORE SUPPORTED IN JDK
//        MimetypesFileTypeMap m = new MimetypesFileTypeMap();
//        String contentType = null;
//        try {
//            contentType = m.getContentType(fileName);
//        } catch (Exception ex) {
//            //ignore
//        }
//        if (contentType == null || "application/octet-stream".equals(contentType)) {
//            if (fileName.endsWith(".txt")) {
//                contentType = "text/plain";
//            } else if (fileName.endsWith(".html")) {
//                contentType = "text/html";
//            } else if (fileName.endsWith(".xml")) {
//                contentType = "text/xml";
//            } else if (fileName.toLowerCase().endsWith(".gif")) {
//                contentType = "image/gif";
//            } else if (fileName.toLowerCase().endsWith(".css")) {
//                contentType = "text/css";
//            } else if (fileName.toLowerCase().endsWith(".js")) {
//                contentType = "text/javascript";
//            } else {
//                contentType = "application/octet-stream";
//            }
//        }
//        return contentType;
    }

    public static String probeContentType(File file) {
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException ex) {
            //ignore
        }
        if (contentType == null || "application/octet-stream".equals(contentType)) {
            return probeContentType(file.getName());
        }
        return contentType;
    }

    public static String probeContentType(URL url) {
        try {
            if (url.getProtocol().equals("file")) {
                File f;
                f = new File(url.toURI());
                return probeContentType(f);
            }
            File temp = File.createTempFile("t", "r");
            Files.copy(url.openStream(), temp.toPath());
            String ct = Files.probeContentType(temp.toPath());
            temp.delete();
            return ct;
        } catch (Exception ex) {
            return null;
        }
    }

    public static File toFileLenient(String path) {
        if (path == null || path.length() == 0) {
            return null;
        }
        if (path.startsWith("/") || path.startsWith("\\")) {
            return new File(path);
        }
        if (path.toLowerCase().startsWith("file:")) {
            Path pa;
            try {
                pa = Paths.get(new URL(path).toURI());
            } catch (Exception ex) {
                return new File(path.substring("file:".length()));
            }
            return pa.toFile();
        }
        if (path.indexOf(':') == 1) {
            for (int i = 0; i < 26; i++) {
                if (path.toLowerCase().startsWith(('a') + ":")) {
                    //windows drive letter
                    return new File(path);
                }
            }
        }
        return null;
    }

    public static File expandFile(String path) {
        final String p = expandPath(path);
        if (p == null) {
            return null;
        }
        return new File(p);
    }

    /**
     * path expansion replaces ~ with ${user.home} property value
     *
     * @param path to expand
     * @return expanded path
     */
    public static String expandPath(String path) {
        if (path == null) {
            return path;
        }
        if (path.equals("~")) {
            return System.getProperty("user.home");
        }
        if (path.startsWith("~") && path.length() > 1 && (path.charAt(1) == '/' || path.charAt(1) == '\\')) {
            return System.getProperty("user.home") + path.substring(1);
        }
        return path;
    }

    public static void replaceInFile(File file, String oldContent, String newContent) throws IOException {
        byte[] bytes = IOUtils.loadByteArray(URLUtils.toURL(file));
        String str = new String(bytes);
        IOUtils.copy(new ByteArrayInputStream(str.replace(oldContent, newContent).getBytes()), file);
    }

    public static void replaceAllInFile(File file, String oldContent, String newContent) throws IOException {
        byte[] bytes = IOUtils.loadByteArray(URLUtils.toURL(file));
        String str = new String(bytes);
        IOUtils.copy(new ByteArrayInputStream(str.replaceAll(oldContent, newContent).getBytes()), file);
    }

    public static void replaceInFolder(File folder, FileFilter fileFilter, boolean recurse, String oldContent, String newContent) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
                    replaceInFile(file, oldContent, newContent);
                } else if (recurse && file.isDirectory()) {
                    replaceInFolder(file, fileFilter, recurse, oldContent, newContent);
                }
            }
        }
    }

    /**
     * retourne le nom du fichier (sans l'extension)
     *
     * @param f fichier
     * @return file name
     */
    public static String getFileBaseName(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i == 0) {
            return "";
        } else if (i > 0) {
            return s.substring(0, i);
        } else {
            return s;
        }
    }

    public static String getFileBaseName(String name) {
        name=getFileName(name);
        int i = name.lastIndexOf('.');
        if (i == 0) {
            return "";
        } else if (i > 0) {
            return name.substring(0, i);
        } else {
            return name;
        }
    }

    public static String getFileParentPath(String name) {
        if(name.equals("/")){
            return null;
        }
        int i = name.lastIndexOf('/');
        if(i>=0){
            name=name.substring(0,i);
            if(name.isEmpty()){
                return "/";
            }
            return name;
        }
        return null;
    }

    public static String getFileName(String name) {
        name=name.replace(File.separatorChar,'/');
        int i = name.lastIndexOf('/');
        if(i>=0){
            name=name.substring(i+1);
        }
        return name;
    }

    /**
     * try to return CanonicalPath otherwise AbsolutePath
     *
     * @param file file
     * @return CanonicalPath or AbsolutePath (if IOException)
     */
    public static String getFilePath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    public static File canonize(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            try {
                return file.getAbsoluteFile();
            } catch (Exception e1) {
                return file;
            }
        }
    }

    public static String getNameWithExtension(String name, String extension) {
        String n = getFileNameWithoutExtension(name);
        String e = getNameExtension(name);
        if (extension == null) {
            return n;
        }
        if (e.equals(extension)) {
            return name;
        }
        return name + "." + extension;
    }

    /**
     * retourne le nom du fichier (sans l'extension)
     *
     * @param f fichier
     * @return file name
     */
    public static String getFileNameWithoutExtension(File f) {
        return getFileNameWithoutExtension(f.getName());
    }

    public static String getFileNameWithoutExtension(String name) {
        if (name == null) {
            name = "";
        }
        int i = name.lastIndexOf('.');
        if (i == 0) {
            return "";
        } else if (i > 0) {
            return name.substring(0, i);
        } else {
            return name;
        }
    }

    public static String getNameExtension(String name) {
        int i = name.lastIndexOf('.');
        if (i == 0) {
            return name.substring(1);
        } else if (i > 0) {
            if (i < (name.length() - 1)) {
                return name.substring(i + 1);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static boolean isFolderWritable(File folder) {
        File ff = null;
        try {
            ff = File.createTempFile("test", "test", folder);
            ff.delete();
            return true;
        } catch (Throwable e) {
            //
        }
        return false;
    }

    public static void mkdirs(File root, String... relativePaths) throws IOException {
        if (!(root.exists() && root.isDirectory())) {
            if (!root.mkdirs()) {
                throw new IOException("Unable to create folder " + root);
            }
        }
        for (String r : relativePaths) {
            File folder = new File(root, r);
            if (!(folder.exists() && folder.isDirectory())) {
                if (!folder.mkdirs()) {
                    throw new IOException("Unable to create folder " + folder);
                }
            }
        }
    }

    public static File createTempFile(URL url) throws IOException {
        String contentType = null;
        if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            contentType = connection.getContentType();
            connection.disconnect();
        }
        String name = URLUtils.getURLName(url);
        String ext = getFileExtension(name);
        if (ext.isEmpty()) {
            if (contentType == null || contentType.trim().isEmpty()) {
                ext = ".unknown";
            } else {
                switch (contentType) {
                    case "application/zip":
                        ext = ".zip";
                        break;
                    case "application/x-rar-compressed": //not supported yet
                        ext = ".rar";
                        break;
                    case "application/java-archive":
                        ext = ".jar";
                        break;
                    default:
                        ext = ".unknown";
                        break;
                }
            }

        } else {
            ext = "." + ext;
        }

        String prefix = name;
        if (ext.length() < 3) {
            ext = "___" + ext;
        }
        if (prefix.length() < 3) {
            prefix = prefix + "___";
        }
        File tempFile = null;
        tempFile = File.createTempFile(prefix, ext);
        IOUtils.copy(url, tempFile, true);
        return tempFile;
    }

    public static String getFileExtension(File f) {
        return getFileExtension(f.getName());
    }

    /**
     * retourne l'extension d'un fichier
     *
     * @return file extension
     */
    public static String getFileExtension(String s) {
        int i = s.lastIndexOf('.');
        if (i == 0) {
            return s.substring(1);
        } else if (i > 0) {
            if (i < (s.length() - 1)) {
                return s.substring(i + 1);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static boolean isFilePath(String path) {
        return path != null && path.indexOf('/') >= 0 && !path.contains("://");
    }

    public static String getAbsolutePath(File cwd, String path) {
        File absoluteFile = getAbsoluteFile(cwd, new File(path));
        try {
            return absoluteFile.getCanonicalPath();
        } catch (IOException e) {
            return absoluteFile.getAbsolutePath();
        }
    }

    public static File getAbsoluteFile(File cwd, String path) {
        return getAbsoluteFile(cwd, new File(path));
    }

    public static File getAbsoluteFile(File cwd, File path) {
        File d=null;
        if (path.isAbsolute()) {
            d=path;
        }else {
            if (cwd == null) {
                cwd = new File(".");
            }
            d=new File(cwd, path.getPath());
        }
        try {
            return d.getCanonicalFile();
        } catch (IOException e) {
            return d.getAbsoluteFile();
        }
    }

    public static String[] expandPath(String path, File cwd) {
        return isFilePath(path) ? findFilePathsOrError(path, cwd, null) : new String[]{path};
    }

    public static String getAbsolutePath(String path) {
        try {
            return getAbsoluteFile(new File(path)).getCanonicalPath();
        } catch (IOException e) {
            return getAbsoluteFile(new File(path)).getAbsolutePath();
        }
    }

    public static File getAbsoluteFile(File path) {
        if (path.isAbsolute()) {
            return path;
        }
        try {
            return path.getCanonicalFile();
        } catch (IOException e) {
            return path.getAbsoluteFile();
        }
    }

    public static boolean isAbsolutePath(String location) {
        return new File(location).isAbsolute();
    }

    public static String getNativePath(String path) {
        return path.replace('/', File.separatorChar);
    }

    /**
     * retourne le path relatif
     *
     * @param parent
     * @param son
     * @return relative path
     */
    public static String getRelativePath(File parent, File son) {
        String parentPath;
        String sonPath;
        try {
            parentPath = parent.getCanonicalPath();
            sonPath = son.getCanonicalPath();
        } catch (IOException e) {
            parentPath = parent.getAbsolutePath();
            sonPath = son.getAbsolutePath();
        }
        if (sonPath.startsWith(parentPath)) {
            String p = sonPath.substring(parentPath.length());
            if (p.startsWith("/") || p.startsWith("\\")) {
                p = p.substring(1);
            }
            return p;
        }
        return null;
    }

    public static void createParents(File f) {
        if (f != null) {
            File pf = f.getParentFile();
            if (pf != null) {
                pf.mkdirs();
            }
        }
    }

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

    public static void copyTree(File in, File out) throws IOException {
        copyTree(in, out, null);
    }

    public static void copyTree(File in, File out, FileFilter filter) throws IOException {
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

                                IOUtils.copy(ins, outs, Math.max(1014 * 1024, (int) x.in.length()));

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
//    public static void copy(InputStream in, OutputStream out) throws IOException {
//        copy(in, out, 4 * 1024);
//    }
//
//    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
//        byte[] buffer = new byte[bufferSize];
//        int r;
//        while ((r = in.read(buffer)) > 0) {
//            out.write(buffer, 0, r);
//        }
//    }

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

    public static String toValidFileName(String name, String defaultName) {
        String r = name==null?"":name;
        if (r.isEmpty()) {
            return defaultName;
        }
        return r
                .replace('/', '_')
                .replace('*', '_')
                .replace('?', '_')
                .replace('.', '_')
                .replace('\\', '_');
    }


    /**
     * should promote this to FileUtils !!
     * @param path
     * @param cwd
     * @return
     */
    public static String getAbsoluteFile2(String path, String cwd) {
        if (new File(path).isAbsolute()) {
            return path;
        }
        if (cwd == null) {
            cwd = System.getProperty("user.dir");
        }
        switch (path){
            case "~" : return System.getProperty("user.home");
            case "." : {
                File file = new File(cwd);
                try {
                    return file.getCanonicalPath();
                }catch (IOException ex){
                    return file.getAbsolutePath();
                }
            }
            case ".." : {
                File file = new File(cwd, "..");
                try {
                    return file.getCanonicalPath();
                }catch (IOException ex){
                    return file.getAbsolutePath();
                }
            }
        }
        int j=-1;
        char[] chars = path.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i]=='/' || chars[i]=='\\'){
                j=i;
                break;
            }
        }
        if(j>0) {
            switch (path.substring(0,j)) {
                case "~":
                    String e = path.substring(j + 1);
                    if(e.isEmpty()){
                        return System.getProperty("user.home");
                    }
                    File file = new File(System.getProperty("user.home"), e);
                    try {
                        return file.getCanonicalPath();
                    }catch (IOException ex){
                        return file.getAbsolutePath();
                    }
            }
        }
        File file = new File(cwd, path);
        try {
            return file.getCanonicalPath();
        }catch (IOException ex){
            return file.getAbsolutePath();
        }
    }
}
