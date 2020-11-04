/**
 * ==================================================================== vpc-prs
 * library
 *
 * Pluggable Resources Set is a small library for simplifying plugin based
 * applications
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.prs.artset;

import java.io.File;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Properties;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com) @creationtime Dec 15, 2007
 * 4:32:41 AM
 */
public class DefaultArtSet implements ArtSet {

    private ClassLoader classLoader;
    private String resourceMask;
    private String id;
    private String name;
    private String group;
    private Object owner;
    private Properties props = new Properties();

    public DefaultArtSet(String id, String name, String group, String resourceMask, ClassLoader classLoader, Object owner) {
        this.classLoader = classLoader;
        this.resourceMask = resourceMask == null ? "*" : resourceMask;
        this.name = name == null ? id : name;
        this.id = id;
        this.group = group;
        this.owner = owner;
    }

    public void setProperty(String id, String value) {
        this.props.setProperty(id, value);
    }

    public String getProperty(String id) {
        return props.getProperty(id);
    }

    public String getArtImageName(String id) {
        return new File(resourceMask.replace("*", id)).getName();
    }

    public URL getArtImageURL(String id) {
        return classLoader.getResource(resourceMask.replace("*", id));
    }

    public ImageIcon getArtImage(String id) {
        URL url = getArtImageURL(id);
        return url == null ? null : new ImageIcon(url);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public Object getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefaultArtSet other = (DefaultArtSet) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "DefaultArtSet{" + "id=" + id + ", name=" + name + '}';
    }
}
