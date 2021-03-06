/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author thevpc
 */
public class _IOUtils {

    public static String loadStreamAsString(URL url) {
        InputStream is = null;
        try {
            try {
                return new String(loadStreamAsByteArray(is = url.openStream()));
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static byte[] loadStreamAsByteArray(InputStream r) {
        ByteArrayOutputStream out = null;
        try {
            try {
                out = new ByteArrayOutputStream();
                copy(r, out, 8096);
                out.flush();
                return out.toByteArray();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static void copy(InputStream in, OutputStream out, int bufferSize) {
        try {
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static Properties loadXMLProperties(File file) {
        try {
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
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
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
