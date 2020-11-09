/**
 * ====================================================================
 *                        vpc-prs library
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
package net.thevpc.common.prs.classloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 19 dec. 2006 22:56:31
 */
public class PluggableResourcesClassLoader extends URLClassLoader {
    private String owner;
    private URL[] urlsList;

    public PluggableResourcesClassLoader(URL url, ClassLoader parent, String owner) throws IOException {
        this(new URL[]{url}, parent, owner);
    }

    public PluggableResourcesClassLoader(URL[] urlsList, ClassLoader parent, String owner) throws IOException {
        super(urlsList, parent);
        this.owner = owner;
        this.urlsList = urlsList;
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public String getOwner() {
        return owner;
    }

    public URL[] getUrlsList() {
        return urlsList;
    }
}

