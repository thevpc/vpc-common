/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
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

import net.thevpc.common.prs.util.PRSPrivateIOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 22 sept. 2007 22:46:59
 */
public class DefaultPluginRepository implements PluginRepository {

    private URL baseURL;
    private int load = UNKNOWN;
    private PluginsList loaded = null;

    public DefaultPluginRepository(URL baseURL) {
        this.baseURL = baseURL;
    }

    public Iterator<PluginDescriptor> getPluginDescriptors() {
        return getPluginsList().pluginDescriptors().iterator();
    }

    public int size() {
        return getPluginsList().size();
    }

    public PluginDescriptor getPluginDescriptor(String id) {
        return getPluginsList().getPluginDescriptor(id);
    }

    public synchronized void refresh() {
        loaded = null;
        getPluginsList();
    }

    private synchronized PluginsList getPluginsList() {
        if (loaded == null) {
            try {
//                Chronometer c = new Chronometer();
                long start = System.currentTimeMillis();
                URL url = new URL(getURL().toString() + "/load");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                InputStream in = null;
                try {
                    in = url.openStream();
                    PRSPrivateIOUtils.copy(in, byteArrayOutputStream);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                long stop = System.currentTimeMillis();
                load = (int) (stop - start);
                loaded = PluginsList.load(getURL());
                if (loaded != null) {
                    for (String o : loaded.keySet()) {
                        loaded.getPluginDescriptor(o).setRepository(this);
                    }
                }
            } catch (IOException e) {
                load = UNREACHABLE;
                System.err.println("Unable to load InstallablePluginsList from " + getURL() + " : " + e.toString());
//                e.printStackTrace();
            }
            if (loaded == null) {
                loaded = new PluginsList();
            }
        }
        return loaded;
    }

    @Override
    public int getLoad() {
        return load;
    }

    @Override
    public URL getURL() {
        return baseURL;
    }

    @Override
    public int compareTo(PluginRepository o) {
        if (o == null) {
            return 1;
        }
        int l1 = getLoad();
        int l2 = o.getLoad();
        int x;
        if (l1 >= 0 && l2 >= 0) {
            x = l1 - l2;
        } else if (l1 < 0 && l2 < 0) {
            x = 0;
        } else if (l1 < 0) {
            x = -1;
        } else {
            x = 1;
        }
        if (x == 0) {
            x = toString().compareTo(o.toString());
        }
        return x > 0 ? 1 : x < 0 ? -1 : 0;
    }

    @Override
    public String toString() {
        return baseURL.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DefaultPluginRepository && ((DefaultPluginRepository) obj).getURL().equals(getURL());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.baseURL != null ? this.baseURL.hashCode() : 0);
        return hash;
    }
}
