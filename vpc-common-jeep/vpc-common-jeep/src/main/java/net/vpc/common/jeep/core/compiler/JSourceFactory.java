/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.compiler;

import net.vpc.common.jeep.JSource;
import net.vpc.common.jeep.impl.compiler.*;

import java.io.File;
import java.net.URL;

/**
 *
 * @author vpc
 */
public class JSourceFactory {

    public static JSource fromString(String text,String sourceName) {
        for (JSource s : rootString(text,sourceName)) {
            return s;
        }
        return null;
    }

    public static JSource fromURL(URL url) {
        for (JSource s : rootURL(url)) {
            return s;
        }
        return null;
    }
    public static JSource fromFile(File file) {
        for (JSource s : rootFile(file)) {
            return s;
        }
        return null;
    }

    public static JSourceRoot rootFile(File file) {
        return new JCompilationUnitFileSource(file);
    }

    public static JSourceRoot rootString(String text, String sourceName) {
        return new JCompilationUnitTextSource(text,sourceName);
    }

    public static JSourceRoot rootURL(URL url) {
        return new JCompilationUnitURLSource(url);
    }

    public static JSourceRoot rootURLFolder(URL url, String fileNameFilter) {
        return new JCompilationUnitURLFolderSource(url, fileNameFilter);
    }

    public static JSourceRoot rootResourceFolder(String url, String fileNameFilter) {
        return new JCompilationUnitResourceFolderSource(url, fileNameFilter);
    }
    public static JSourceRoot rootResourceFile(String url) {
        return new JCompilationUnitResourceFileSource(url);
    }
}
