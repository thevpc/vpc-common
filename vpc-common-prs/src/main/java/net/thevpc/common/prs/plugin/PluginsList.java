/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.xml.XmlUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 sept. 2007 23:13:00
 */
public class PluginsList implements Serializable {
    protected HashMap<String, PluginDescriptor> dataMap = new HashMap<String, PluginDescriptor>();

    public int size() {
        return dataMap.size();
    }

    public Set<String> keySet() {
        return dataMap.keySet();
    }


    public PluginDescriptor getPluginDescriptor(String p) {
        return dataMap.get(p);
    }

    public void addPluginDescriptor(PluginDescriptor p) {
        dataMap.put(p.getId(), p);
    }

    public Collection<PluginDescriptor> pluginDescriptors() {
        return dataMap.values();
    }

    public boolean containsProperty(String p) {
        return dataMap.containsKey(p);
    }

    public void unsetProperty(String p) {
        dataMap.remove(p);
    }


    public void store(File folder, File binaryFolder, File srcFolder) throws IOException {
        for (PluginDescriptor descriptor : dataMap.values()) {

        }
        store(folder);
    }

    public void store(File folder) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(new File(folder, "repository.xml.zip")));
            out.putNextEntry(new ZipEntry("repository.xml"));
            XmlUtils.objectToXml(this, out, null, null, false);
            out.closeEntry();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static PluginsList load(URL url) throws IOException {
        url = new URL(url.toString() + "/repository.xml.zip");
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(url.openStream());
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().equals("repository.xml")) {
                    return (PluginsList) XmlUtils.xmlToObject(zis, null, null);
                }
                zis.closeEntry();
            }
        } finally {
            if (zis != null) {
                zis.close();
            }
        }
        return null;
    }

}
