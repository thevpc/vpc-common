/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
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
package net.thevpc.common.swing.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 7 janv. 2007 18:31:14
 */
public class ExtensionFileChooserFilter extends javax.swing.filechooser.FileFilter
        implements FilenameFilter, java.io.FileFilter {
    private String extension;
    private String description;

    public ExtensionFileChooserFilter(String extension, String description) {
        if (extension == null) {
            extension = "";
        }
        this.extension = extension;
        if (description == null) {
            description = "Files " + extension;
        }
        this.description = description;
    }

    public boolean accept(File f) {
        //TODO is it correct to handle such case ??
        if (f.isDirectory()) {
            String[] strings = f.list();
            return strings != null && strings.length > 0;//true;
        }

        String e = getExtension(f);
        if (e == null) e = "";
        return e.equalsIgnoreCase(extension);
    }

    public boolean accept(File dir, String name) {
        String e = getExtension(name);
        if (e == null) e = "";
        return e.equalsIgnoreCase(extension);
    }

    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }

    public String getExtension(String filename) {
        if (filename != null) {
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }

    public String getDescription() {
        return description;
    }
}

