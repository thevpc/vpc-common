/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thevpc
 */
public class FilePathTextAutoComplete implements TextAutoComplete {
    
    @Override
    public List<String> autoComplete(String current) {
        List<String> a = new ArrayList<>();
        if (current == null || current.isEmpty()) {
            for (File file : File.listRoots()) {
                a.add(file.getPath());
            }
        } else {
            boolean isFile = false;
            for (File listRoot : File.listRoots()) {
                if (current.startsWith(listRoot.getPath())) {
                    isFile = true;
                    break;
                }
            }
            if (isFile) {
                File z = null;
                z = new File(current);
                if (current.endsWith("/") || current.endsWith("\\")) {
                    File[] lf = z.listFiles();
                    if (lf != null) {
                        for (File file : lf) {
                            a.add(file.getPath());
                        }
                    }
                } else {
                    z = z.getParentFile();
                    File[] lf = z.listFiles();
                    if (lf != null) {
                        for (File file : lf) {
                            if (file.getPath().startsWith(current)) {
                                a.add(file.getPath());
                            }
                        }
                    }
                    if (a.size() == 1 && new File(a.get(0)).isDirectory()) {
                        a.set(0, a.get(0) + File.separator);
                    }
                }
            }
        }
        return a;
    }
    
}
