/**
 * ====================================================================
 * vpc-commons library
 *
 * Description: <start><end>
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.prs.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Classe utilitaire pour la manipulation des E/S
 *
 * @author tbensalah (Taha Ben Salah)
 * %creation_date date 27/01/2004
 * %last_modification_date date 28/01/2004
 */
public final class PRSPrivateIOUtils {

    /**
     * taille par defaut du buffer de transfert
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out, int bufferSize) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
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
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out, int bufferSize) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(in);
            copy(fis, out, bufferSize);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static void copy(URL url, File out) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            PRSPrivateIOUtils.copy(in, out);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out, int bufferSize) throws IOException {
        FileOutputStream fos = null;
        try {
            File p = out.getParentFile();
            if (p != null) {
                p.mkdirs();
            }
            fos = new FileOutputStream(out);
            copy(in, fos, bufferSize);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * retourne le nom du fichier (sans l'extension)
     *
     * @param f fichier
     * @return file name
     */
    public static String getFileNameWithoutExtension(File f) {
        return getNameWithoutExtension(f.getName());
    }

    public static String getNameWithoutExtension(String name) {
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

    /**
     * retourne l'extension d'un fichier
     *
     * @param f fichier
     * @return file extension
     */
    public static String getFileExtension(File f) {
        return getNameExtension(f.getName());
    }

    public static String getNameWithExtension(String name, String extension) {
        String n = getNameWithoutExtension(name);
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

    /**
     * try to return CanonicaFile otherwise AbsoluteFile
     *
     * @param file file
     * @return CanonicalFile or AbsoluteFile (if IOException)
     */
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

    public static byte[] loadStreamAsByteArray(URL url) throws IOException {
        InputStream r = null;
        try {
            return loadStreamAsByteArray(r = url.openStream());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    public static byte[] loadStreamAsByteArray(InputStream r) throws IOException {
        ByteArrayOutputStream out = null;
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
    }

    public static void storeXMLProperties(File file, Properties p, String comments) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            p.storeToXML(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static void storeProperties(File file, Properties p, String comments) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            p.store(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static Properties loadXMLProperties(File file) throws IOException {
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            p.loadFromXML(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return p;
    }

    public static Properties loadProperties(File file) throws IOException {
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            p.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return p;
    }

    public static String loadStreamAsString(URL url) throws IOException {
        return new String(loadStreamAsByteArray(url));
    }

    public static String loadStreamAsString(InputStream r) throws IOException {
        return new String(loadStreamAsByteArray(r));
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

    public static void replaceInFile(File file, String oldContent, String newContent) throws IOException {
        byte[] bytes = loadStreamAsByteArray(file.toURL());
        String str = new String(bytes);
        copy(new ByteArrayInputStream(str.replace(oldContent, newContent).getBytes()), file);
    }

    public static void replaceAllInFile(File file, String oldContent, String newContent) throws IOException {
        byte[] bytes = loadStreamAsByteArray(file.toURL());
        String str = new String(bytes);
        copy(new ByteArrayInputStream(str.replaceAll(oldContent, newContent).getBytes()), file);
    }

    public static void saveObject(File physicalName, Object object) throws IOException {
        ObjectOutputStream oos = null;
        try {
            File parentFile = physicalName.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            oos = new ObjectOutputStream(new FileOutputStream(physicalName));
            oos.writeObject(object);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    public static Object loadObject(File physicalName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(physicalName));
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static void saveString(File physicalName, String object) throws IOException {
        PrintWriter oos = null;
        try {
            oos = new PrintWriter(new FileWriter(physicalName));
            oos.print(object);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    public static String loadString(File physicalName) throws IOException {
        FileReader ois = null;
        try {
            ois = new FileReader(physicalName);
            StringBuilder s = new StringBuilder();
            int i = 0;
            int max = (int) physicalName.length();
            if (max <= 256) {
                max = 256;
            }
            char[] all = new char[max];
            while ((i = ois.read(all)) > 0) {
                s.append(all, 0, i);
            }
            return s.toString();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static void saveZippedObject(File physicalName, Object object) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(physicalName)));
            oos.writeObject(object);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    public static Object loadZippedObject(File physicalName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(physicalName)));
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static String computeCRC(File file) throws IOException {
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(file);
            return computeCRC(file);
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    public static String computeCRC(byte[] bytes) throws IOException {
        InputStream f = null;
        try {
            f = new ByteArrayInputStream(bytes);
            return computeCRC(f);
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    public static String computeCRC(InputStream is) throws IOException {
        CheckedInputStream cis = null;
        try {
            cis = new CheckedInputStream(is, new CRC32());
            byte[] buf = new byte[1024];
            int c;
            while ((c = cis.read(buf)) >= 0) {
                //
            }
            return Long.toString(cis.getChecksum().getValue(), 32).toUpperCase();
        } finally {
            if (cis != null) {
                cis.close();
            }
        }
    }

    public static String computeCRC(Reader reader) throws IOException {
        try {
            char[] buf = new char[1024];
            int c;
            CRC32 crc = new CRC32();
            while ((c = reader.read(buf)) > 0) {
                crc.update(new String(buf, 0, c).getBytes());
                //
            }
            return Long.toString(crc.getValue(), 32).toUpperCase();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static boolean isFileURL(URL repositoryURL) {
        return "file".equalsIgnoreCase(repositoryURL.getProtocol());
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

    public static String getURLPath(URL repositoryURL) {
        if ("file".equalsIgnoreCase(repositoryURL.getProtocol())) {
            File folder = new File(repositoryURL.getFile());
            try {
                return (folder.getCanonicalPath());
            } catch (IOException e) {
                return (folder.getAbsolutePath());
            }
        } else {
            return (repositoryURL.toString());
        }
    }

    public static URL fileToURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException ex) {
            //should never happen
            throw new IllegalArgumentException(ex);
        }
    }

    public static File urlToFile(URL url) {
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            return new File(URLDecoder.decode(url.getFile()));
        }
        return null;
    }

    public static String getURLName(URL url) {
        String path = url.getFile();
        String name;
        int index = path.lastIndexOf('/');
        if (index < 0) {
            name = path;
        } else {
            name = path.substring(index + 1);
        }
        index = name.indexOf('?');
        if (index >= 0) {
            name = name.substring(0, index);
        }
        name = name.trim();
        return name;
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

    private PRSPrivateIOUtils() {
    }
}
