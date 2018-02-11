/**
 * ====================================================================
 *                        vpc-prs library
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
package net.vpc.common.prs.classloader;

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

