///**
// * ====================================================================
// * vpc-commons library
// *
// * Description: <start><end>
// *
// * Copyright (C) 2006-2008 Taha BEN SALAH
// *
// * This program is free software; you can redistribute it and/or modify it under
// * the terms of the GNU General Public License as published by the Free Software
// * Foundation; either version 2 of the License, or (at your option) any later
// * version.
// *
// * This program is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// * details.
// *
// * You should have received a copy of the GNU General Public License along with
// * this program; if not, write to the Free Software Foundation, Inc., 51
// * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
// * ====================================================================
// */
//package net.thevpc.common.swings.util;
//
//import java.io.*;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.util.Properties;
//import java.util.zip.CRC32;
//import java.util.zip.CheckedInputStream;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;
//
///**
// * Classe utilitaire pour la manipulation des E/S
// *
// * @author tbensalah (Taha Ben Salah)
// * @creation_date date 27/01/2004
// * @last_modification_date date 28/01/2004
// */
//public final class IOUtils {
//
//    /**
//     * taille par defaut du buffer de transfert
//     */
//    public static final int DEFAULT_BUFFER_SIZE = 1024;
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(InputStream in, OutputStream out) throws IOException {
//        copy(in, out, DEFAULT_BUFFER_SIZE);
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
//        byte[] buffer = new byte[bufferSize];
//        int len;
//        while ((len = in.read(buffer)) > 0) {
//            out.write(buffer, 0, len);
//        }
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(File in, File out) throws IOException {
//        copy(in, out, DEFAULT_BUFFER_SIZE);
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(File in, File out, int bufferSize) throws IOException {
//        FileInputStream fis = null;
//        FileOutputStream fos = null;
//        try {
//            fis = new FileInputStream(in);
//            fos = new FileOutputStream(out);
//            copy(fis, fos, bufferSize);
//        } finally {
//            if (fis != null) {
//                fis.close();
//            }
//            if (fos != null) {
//                fos.close();
//            }
//        }
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(File in, OutputStream out) throws IOException {
//        copy(in, out, DEFAULT_BUFFER_SIZE);
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(File in, OutputStream out, int bufferSize) throws IOException {
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(in);
//            copy(fis, out, bufferSize);
//        } finally {
//            if (fis != null) {
//                fis.close();
//            }
//        }
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(InputStream in, File out) throws IOException {
//        copy(in, out, DEFAULT_BUFFER_SIZE);
//    }
//
//    public static void copy(URL url, File out) throws IOException {
//        InputStream in = null;
//        try {
//            in = url.openStream();
//            IOUtils.copy(in, out);
//        } finally {
//            if (in != null) {
//                in.close();
//            }
//        }
//    }
//
//    /**
//     * copy le flux d'entree dans le lux de sortie
//     *
//     * @param in entree
//     * @param out sortie
//     * @throws IOException when IO error
//     */
//    public static void copy(InputStream in, File out, int bufferSize) throws IOException {
//        FileOutputStream fos = null;
//        try {
//            File p = out.getParentFile();
//            if (p != null) {
//                p.mkdirs();
//            }
//            fos = new FileOutputStream(out);
//            copy(in, fos, bufferSize);
//        } finally {
//            if (fos != null) {
//                fos.close();
//            }
//        }
//    }
//
//
//    /**
//     * retourne l'extension d'un fichier
//     *
//     * @param f fichier
//     * @return file extension
//     */
//    public static String getFileExtension(File f) {
//        return getNameExtension(f.getName());
//    }
//
//
//
//    /**
//     * try to return CanonicaFile otherwise AbsoluteFile
//     *
//     * @param file file
//     * @return CanonicalFile or AbsoluteFile (if IOException)
//     */
//    public static File canonize(File file) {
//        try {
//            return file.getCanonicalFile();
//        } catch (IOException e) {
//            try {
//                return file.getAbsoluteFile();
//            } catch (Exception e1) {
//                return file;
//            }
//        }
//    }
//
//    /**
//     * retourne le path relatif
//     *
//     * @param parent
//     * @param son
//     * @return relative path
//     */
//    public static String getRelativePath(File parent, File son) {
//        String parentPath;
//        String sonPath;
//        try {
//            parentPath = parent.getCanonicalPath();
//            sonPath = son.getCanonicalPath();
//        } catch (IOException e) {
//            parentPath = parent.getAbsolutePath();
//            sonPath = son.getAbsolutePath();
//        }
//        if (sonPath.startsWith(parentPath)) {
//            String p = sonPath.substring(parentPath.length());
//            if (p.startsWith("/") || p.startsWith("\\")) {
//                p = p.substring(1);
//            }
//            return p;
//        }
//        return null;
//    }
//
//    public static byte[] loadStreamAsByteArray(URL url) throws IOException {
//        InputStream r = null;
//        try {
//            return loadStreamAsByteArray(r = url.openStream());
//        } finally {
//            if (r != null) {
//                r.close();
//            }
//        }
//    }
//
//    public static byte[] loadStreamAsByteArray(InputStream r) throws IOException {
//        ByteArrayOutputStream out = null;
//        try {
//            out = new ByteArrayOutputStream();
//            copy(r, out);
//            out.flush();
//            return out.toByteArray();
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//    }
//
//    public static void storeXMLProperties(File file, Properties p, String comments) throws IOException {
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(file);
//            p.storeToXML(os, comments);
//        } finally {
//            if (os != null) {
//                os.close();
//            }
//        }
//    }
//
//    public static void storeProperties(File file, Properties p, String comments) throws IOException {
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(file);
//            p.store(os, comments);
//        } finally {
//            if (os != null) {
//                os.close();
//            }
//        }
//    }
//
//    public static Properties loadXMLProperties(File file) throws IOException {
//        Properties p = new Properties();
//        FileInputStream is = null;
//        try {
//            is = new FileInputStream(file);
//            p.loadFromXML(is);
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//        return p;
//    }
//
//    public static Properties loadProperties(File file) throws IOException {
//        Properties p = new Properties();
//        FileInputStream is = null;
//        try {
//            is = new FileInputStream(file);
//            p.load(is);
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//        return p;
//    }
//
//    public static String loadStreamAsString(URL url) throws IOException {
//        return new String(loadStreamAsByteArray(url));
//    }
//
//    public static String loadStreamAsString(InputStream r) throws IOException {
//        return new String(loadStreamAsByteArray(r));
//    }
//
//    public static void replaceInFolder(File folder, FileFilter fileFilter, boolean recurse, String oldContent, String newContent) throws IOException {
//        File[] files = folder.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
//                    replaceInFile(file, oldContent, newContent);
//                } else if (recurse && file.isDirectory()) {
//                    replaceInFolder(file, fileFilter, recurse, oldContent, newContent);
//                }
//            }
//        }
//    }
//
//    public static boolean deleteFolderTree(File folder, FileFilter fileFilter) {
//        if (!folder.exists()) {
//            return true;
//        }
//        File[] files = folder.listFiles(fileFilter);
//        boolean ok = true;
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
//                    if (!file.delete()) {
//                        ok = false;
//                    }
//                } else if (file.isDirectory()) {
//                    deleteFolderTree(file, fileFilter);
//                } else {
//                    ok = false;
//                }
//            }
//        }
//        return ok && folder.delete();
//    }
//
//    public static void replaceInFile(File file, String oldContent, String newContent) throws IOException {
//        byte[] bytes = loadStreamAsByteArray(file.toURL());
//        String str = new String(bytes);
//        copy(new ByteArrayInputStream(str.replace(oldContent, newContent).getBytes()), file);
//    }
//
//    public static void replaceAllInFile(File file, String oldContent, String newContent) throws IOException {
//        byte[] bytes = loadStreamAsByteArray(file.toURL());
//        String str = new String(bytes);
//        copy(new ByteArrayInputStream(str.replaceAll(oldContent, newContent).getBytes()), file);
//    }
//
//    public static void saveObject(File physicalName, Object object) throws IOException {
//        ObjectOutputStream oos = null;
//        try {
//            File parentFile = physicalName.getParentFile();
//            if (parentFile != null) {
//                parentFile.mkdirs();
//            }
//            oos = new ObjectOutputStream(new FileOutputStream(physicalName));
//            oos.writeObject(object);
//            oos.close();
//        } finally {
//            if (oos != null) {
//                oos.close();
//            }
//        }
//    }
//
//    public static Object loadObject(File physicalName) throws IOException, ClassNotFoundException {
//        ObjectInputStream ois = null;
//        try {
//            ois = new ObjectInputStream(new FileInputStream(physicalName));
//            return ois.readObject();
//        } finally {
//            if (ois != null) {
//                ois.close();
//            }
//        }
//    }
//
//    public static void saveString(File physicalName, String object) throws IOException {
//        PrintWriter oos = null;
//        try {
//            oos = new PrintWriter(new FileWriter(physicalName));
//            oos.print(object);
//            oos.close();
//        } finally {
//            if (oos != null) {
//                oos.close();
//            }
//        }
//    }
//
//    public static String loadString(File physicalName) throws IOException {
//        FileReader ois = null;
//        try {
//            ois = new FileReader(physicalName);
//            StringBuilder s = new StringBuilder();
//            int i = 0;
//            int max = (int) physicalName.length();
//            if (max <= 256) {
//                max = 256;
//            }
//            char[] all = new char[max];
//            while ((i = ois.read(all)) > 0) {
//                s.append(all, 0, i);
//            }
//            return s.toString();
//        } finally {
//            if (ois != null) {
//                ois.close();
//            }
//        }
//    }
//
//    public static void saveZippedObject(File physicalName, Object object) throws IOException {
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(physicalName)));
//            oos.writeObject(object);
//            oos.close();
//        } finally {
//            if (oos != null) {
//                oos.close();
//            }
//        }
//    }
//
//    public static Object loadZippedObject(File physicalName) throws IOException, ClassNotFoundException {
//        ObjectInputStream ois = null;
//        try {
//            ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(physicalName)));
//            return ois.readObject();
//        } finally {
//            if (ois != null) {
//                ois.close();
//            }
//        }
//    }
//
//
////    public static void main(String[] args) {
////        try {
////            replaceInFolder(new File("/home/vpc/xprojects/apps/dbclient/src"),new FileFilter() {
////                public boolean accept(File pathname) {
////                    return pathname.getName().toLowerCase().endsWith(".java");
////                }
////            },      true,
////                            "package net.thevpc.dbclient",
////                    "/**\n" +
////                            " * ====================================================================\n" +
////                            " *             DBCLient yet another Jdbc client tool\n" +
////                            " *\n" +
////                            " * DBClient is a new Open Source Tool for connecting to jdbc\n" +
////                            " * compliant relational databases. Specific extensions will take care of\n" +
////                            " * each RDBMS implementation.\n" +
////                            " *\n" +
////                            " * Copyright (C) 2006-2007 Taha BEN SALAH\n" +
////                            " *\n" +
////                            " * This program is free software; you can redistribute it and/or modify\n" +
////                            " * it under the terms of the GNU General Public License as published by\n" +
////                            " * the Free Software Foundation; either version 2 of the License, or\n" +
////                            " * (at your option) any later version.\n" +
////                            " *\n" +
////                            " * This program is distributed in the hope that it will be useful,\n" +
////                            " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
////                            " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
////                            " * GNU General Public License for more details.\n" +
////                            " *\n" +
////                            " * You should have received a copy of the GNU General Public License along\n" +
////                            " * with this program; if not, write to the Free Software Foundation, Inc.,\n" +
////                            " * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n" +
////                            " * ====================================================================\n" +
////                            " */\n" +
////                            "\n" +
////                            "package net.thevpc.dbclient"
////                    );
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
//
//    public static String getURLPath(URL repositoryURL) {
//        if ("file".equalsIgnoreCase(repositoryURL.getProtocol())) {
//            File folder = new File(repositoryURL.getFile());
//            try {
//                return (folder.getCanonicalPath());
//            } catch (IOException e) {
//                return (folder.getAbsolutePath());
//            }
//        } else {
//            return (repositoryURL.toString());
//        }
//    }
//
//    public static URL fileToURL(File file) {
//        try {
//            return file.toURI().toURL();
//        } catch (MalformedURLException ex) {
//            //should never happen
//            throw new IllegalArgumentException(ex);
//        }
//    }
//
//    public static File urlToFile(URL url) {
//        String protocol = url.getProtocol();
//        if ("file".equals(protocol)) {
//            return new File(URLDecoder.decode(url.getFile()));
//        }
//        return null;
//    }
//
//

//    private IOUtils() {
//    }
//}
