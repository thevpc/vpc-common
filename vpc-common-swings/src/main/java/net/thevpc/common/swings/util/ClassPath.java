/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author thevpc
 */
public class ClassPath extends ArrayList<String> {

    public static ClassPath getSystemClassPath() {
        return parse(System.getProperty("java.class.path"));
    }

    public static ClassPath parse(String classPathString) {
        return parse(classPathString, null);
    }

    public static ClassPath parse(String classPathString, String sep) {
        if (sep == null) {
            sep = File.pathSeparator;
        }
        ClassPath path = new ClassPath();
        StringTokenizer s = new StringTokenizer(classPathString, sep);
        while (s.hasMoreElements()) {
            String c = s.nextToken();
            if (c != null && !c.isEmpty()) {
                path.add(c);
            }
        }
        return path;
    }

    public void removeDuplicates() {
        HashSet<String> found = new HashSet<String>();
        for (Iterator<String> i = iterator(); i.hasNext();) {
            String s = i.next();
            if (found.contains(s)) {
                i.remove();
            } else {
                found.add(s);
            }
        }
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(String sep) {
        if (sep == null) {
            sep = File.pathSeparator;
        }
        StringBuilder s = new StringBuilder();
        boolean empty = true;
        for (String p : this) {
            if (empty) {
                s.append(p);
                empty=false;
            } else {
                s.append(sep);
                s.append(p);
            }
        }
        return s.toString();
    }

    public void addFile(File file) {
        add(file.getPath());
    }

    public void addFiles(File... files) {
        for (File file : files) {
            addFile(file);
        }
    }

    public void addURL(URL file) {
        add(file.getFile());
    }

    public void addURLs(URL... files) {
        for (URL file : files) {
            addURL(file);
        }
    }
}
