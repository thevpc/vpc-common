/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author thevpc
 */
public class SwingPrivateIOUtils {

    public static String loadStreamAsString(URL url) throws IOException {
        InputStream io = null;
        try {
            io = url.openStream();
            return new String(loadStreamAsByteArray(io));
        } finally {
            if (io != null) {
                io.close();
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

}
