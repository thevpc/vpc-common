/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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
 * %creationtime 22 sept. 2007 23:13:00
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
