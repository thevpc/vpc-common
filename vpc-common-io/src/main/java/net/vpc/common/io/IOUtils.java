/*
     * To change this license header, choose License Headers in Project Properties.
     *
     * and open the template in the editor.
 */
package net.vpc.common.io;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

/**
 * @author taha.bensalah@gmail.com
 */
public class IOUtils {

    /**
     * taille par defaut du buffer de transfert
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public static String getCanonicalPath(File fileName) {
        try {
            return fileName.getCanonicalPath();
        } catch (IOException e) {
            return fileName.getAbsolutePath();
        }
    }

    public static String probeContentType(String fileName) {
        MimetypesFileTypeMap m = new MimetypesFileTypeMap();
        String contentType = null;
        try {
            contentType = m.getContentType(fileName);
        } catch (Exception ex) {
            //ignore
        }
        if (contentType == null || "application/octet-stream".equals(contentType)) {
            if (fileName.endsWith(".txt")) {
                contentType = "text/plain";
            } else if (fileName.endsWith(".html")) {
                contentType = "text/html";
            } else if (fileName.endsWith(".xml")) {
                contentType = "text/xml";
            } else if (fileName.toLowerCase().endsWith(".gif")) {
                contentType = "image/gif";
            } else if (fileName.toLowerCase().endsWith(".css")) {
                contentType = "text/css";
            } else if (fileName.toLowerCase().endsWith(".js")) {
                contentType = "text/javascript";
            } else {
                contentType = "application/octet-stream";
            }
        }
        return contentType;
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

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    public static Iterable<String> toStringIterable(InputStreamSource is, boolean trackChanges) throws IOException {
        if (!trackChanges) {
            return new InputStreamSourceStringIterable(is);
        } else {

            return new TextMonitor(is);
        }
    }

    public static Iterable<String> filterExceptions(InputStreamSource is, boolean trackChanges) throws IOException {
        if (!trackChanges) {
            return new InputStreamSourceStringIterable(is);
        } else {

            return new TextMonitor(is);
        }
    }

    public static String[] toStringArray(InputStreamSource is) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is.open()))) {
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = r.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(new String[lines.size()]);
        }
    }

    public static Properties toProperties(InputStreamSource is) throws IOException {
        try (InputStream r = is.open()) {
            Properties p = new Properties();
            p.load(r);
            return p;
        }
    }

    public static Properties toPropertiesXML(InputStreamSource is) throws IOException {
        try (InputStream r = is.open()) {
            Properties p = new Properties();
            p.loadFromXML(r);
            return p;
        }
    }

    public static String readString(File is) throws IOException {
        return toString(toInputStreamSource(is));
    }

    public static void writeString(File is, String string) throws IOException {
        try (PrintStream p = new PrintStream(is)) {
            p.print(string);
        }
    }

    public static String toString(InputStreamSource is) throws IOException {
        return new String(toCharArray(is));
    }

    public static String toString(InputStream is) throws IOException {
        return new String(toCharArray(is));
    }

    public static char[] toCharArray(InputStreamSource is) throws IOException {
        try (InputStream in = is.open()) {
            return toCharArray(in);
        }
    }

    public static char[] toCharArray(InputStream is) throws IOException {
        return toCharArray(new InputStreamReader(is));
    }

    public static String toString(Reader r) throws IOException {
        return new String(toCharArray(r));
    }

    public static char[] toCharArray(Reader r) throws IOException {
        CharArrayWriter buffer = new CharArrayWriter();

        int nRead;
        char[] data = new char[16384];

        while ((nRead = r.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toCharArray();
    }

    @Deprecated
    public static byte[] read(InputStream in, int count) throws IOException {
        byte[] buffer = new byte[count];
        int i = in.read(buffer);
        byte[] buffer2 = new byte[i];
        System.arraycopy(buffer, 0, buffer2, 0, i);
        return buffer2;
    }

    public static byte[] readByteArray(InputStream in, int count) throws IOException {
        byte[] buffer = new byte[count];
        int i = in.read(buffer);
        byte[] buffer2 = new byte[i];
        System.arraycopy(buffer, 0, buffer2, 0, i);
        return buffer2;
    }

    public static char[] readCharArray(InputStream in, int count) throws IOException {
        return readCharArray(new InputStreamReader(in), count);
    }

    public static char[] readCharArray(Reader in, int count) throws IOException {
        char[] buffer = new char[count];
        int i = in.read(buffer);
        char[] buffer2 = new char[i];
        System.arraycopy(buffer, 0, buffer2, 0, i);
        return buffer2;
    }

    public static OutputStreamSource toOutputStreamSource(final File file) {
        return new OutputStreamSource() {
            @Override
            public OutputStream open() throws IOException {
                return new FileOutputStream(file);
            }

            @Override
            public Object getSource() throws IOException {
                return file;
            }

        };
    }

    public static InputStreamSource toInputStreamSource(byte[] bytes) {
        return new ByteArrayInputStreamSource(bytes);
    }

    public static InputStreamSource toInputStreamSource(String string) {
        return new StringInputStreamSource(string);
    }

    public static InputStreamSource toInputStreamSource(File file) {
        return new FileInputStreamSource(file);
    }

    public static InputStreamSource toInputStreamSource(URL url) {
        return new URLInputStreamSource(url);
    }

    public static InputStreamSource toInputStreamSource(InputStream stream) {
        return new SingleAccessInputStreamSource(stream);
    }

    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException ex) {
            return false;
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

    public static String getUrlProtocol(String path) {
        if (path == null) {
            return null;
        }
        int i = path.indexOf("://");
        if (i > 0) {
            String protocol = path.substring(0, i).trim();
            if (protocol.matches("[a-zA-Z0-9_-]+")) {
                return protocol;
            }
        }
        return null;
    }

    public static String getUrlFile(String path) {
        if (path == null) {
            return null;
        }
        int i = path.indexOf("://");
        if (i > 0) {
            return path.substring(i + 3);
        }
        return path;
    }

    public static void createParents(File f) {
        if (f != null) {
            File pf = f.getParentFile();
            if (pf != null) {
                pf.mkdirs();
            }
        }
    }

    private static class InputStreamSourceStringIterable implements Iterable<String> {

        private final InputStreamSource is;

        public InputStreamSourceStringIterable(InputStreamSource is) {
            this.is = is;
        }

        @Override
        public Iterator<String> iterator() {
            try {
                return new InputStreamStringIterator(is.open());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static File expandFile(String path) {
        final String p = expandPath(path);
        if(p==null){
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

    public static void saveObject2(String physicalName, Object object) {
        try {
            saveObject(physicalName, object);
        } catch (Exception e) {
            e.printStackTrace();
            //ignore
        }
    }

    public static void saveObject(String physicalName, Object object) throws RuntimeIOException {
        physicalName = expandPath(physicalName);
        try {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(physicalName));
                oos.writeObject(object);
                oos.close();
            } finally {
                if (oos != null) {
                    oos.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static void saveString(String physicalName, String object) throws RuntimeIOException {
        physicalName = expandPath(physicalName);
        try {
            PrintStream oos = null;
            try {
                oos = new PrintStream(new File(physicalName));
                oos.print(object);
                oos.close();
            } finally {
                if (oos != null) {
                    oos.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static Object loadObject2(String physicalName) {
        physicalName = expandPath(physicalName);
        try {
            File f = new File(physicalName);
            if (f.isFile()) {
                return loadObject(physicalName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object loadObject(String physicalName) throws RuntimeIOException, ClassNotFoundException {
        physicalName = expandPath(physicalName);
        ObjectInputStream ois = null;
        try {
            try {
                ois = new ObjectInputStream(new FileInputStream(physicalName));
                return ois.readObject();
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static Object loadZippedObject(String physicalName) throws RuntimeIOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            try {
                ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(expandPath(physicalName))));
                return ois.readObject();
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

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

    public static URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static void replaceInFile(File file, String oldContent, String newContent) throws RuntimeIOException {
        byte[] bytes = loadStreamAsByteArray(toURL(file));
        String str = new String(bytes);
        copy(new ByteArrayInputStream(str.replace(oldContent, newContent).getBytes()), file);
    }

    public static void replaceAllInFile(File file, String oldContent, String newContent) throws RuntimeIOException {
        byte[] bytes = loadStreamAsByteArray(toURL(file));
        String str = new String(bytes);
        copy(new ByteArrayInputStream(str.replaceAll(oldContent, newContent).getBytes()), file);
    }

    public static String loadStreamAsString(URL url) throws RuntimeIOException {
        return new String(loadStreamAsByteArray(url));
    }

    public static String loadStreamAsString(InputStream r) throws RuntimeIOException {
        return new String(loadStreamAsByteArray(r));
    }

    public static void replaceInFolder(File folder, FileFilter fileFilter, boolean recurse, String oldContent, String newContent) throws RuntimeIOException {
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

    public static byte[] loadStreamAsByteArray(URL url) throws RuntimeIOException {
        InputStream r = null;
        try {
            try {
                return loadStreamAsByteArray(url);
            } finally {
                if (r != null) {
                    r.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static byte[] loadStreamAsByteArray(InputStream r) throws RuntimeIOException {
        ByteArrayOutputStream out = null;
        try {
            try {
                out = new ByteArrayOutputStream();
                copy(r, out);
                out.flush();
                return out.toByteArray();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static void storeXMLProperties(File file, Properties p, String comments) throws RuntimeIOException {
        FileOutputStream os = null;
        try {
            try {
                os = new FileOutputStream(file);
                p.storeToXML(os, comments);
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static void storeProperties(File file, Properties p, String comments) throws RuntimeIOException {
        FileOutputStream os = null;
        try {
            try {
                os = new FileOutputStream(file);
                p.store(os, comments);
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static Properties loadXMLProperties(File file) throws RuntimeIOException {
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            try {
                is = new FileInputStream(file);
                p.loadFromXML(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
        return p;
    }

    public static Properties loadProperties(File file) throws RuntimeIOException {
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            try {
                is = new FileInputStream(file);
                p.load(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
        return p;
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out) throws RuntimeIOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out, int bufferSize) throws RuntimeIOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        try {
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out) throws RuntimeIOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out, int bufferSize) throws RuntimeIOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            try {
                fis = new FileInputStream(in);
                fos = new FileOutputStream(out);
                copy(fis, fos, bufferSize);
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out) throws RuntimeIOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out, int bufferSize) throws RuntimeIOException {
        FileInputStream fis = null;
        try {
            try {
                fis = new FileInputStream(in);
                copy(fis, out, bufferSize);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out) throws RuntimeIOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static void copy(URL url, File out) throws RuntimeIOException {
        InputStream in = null;
        try {
            try {
                in = url.openStream();
                IOUtils.copy(in, out);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out, int bufferSize) throws RuntimeIOException {
        FileOutputStream fos = null;
        try {
            try {
                fos = new FileOutputStream(out);
                copy(in, fos, bufferSize);
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    /**
     * retourne le nom du fichier (sans l'extension)
     *
     * @param f fichier
     * @return file name
     */
    public static String getFileName(File f) {
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

    /**
     * retourne l'extension d'un fichier
     *
     * @param f fichier
     * @return file extension
     */
    public static String getFileExtension(File f) {
        String s = f.getName();
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

    public static void writeToFile(String str, File file) throws IOException {
        if (str == null) {
            str = "";
        }
        try (PrintStream printStream = new PrintStream(file)) {
            printStream.print(str);
        }
    }

}
