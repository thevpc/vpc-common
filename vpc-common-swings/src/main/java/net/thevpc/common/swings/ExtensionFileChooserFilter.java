/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.swings;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 7 janv. 2007 18:31:14
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

