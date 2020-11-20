/**
 * ==================================================================== vpc-prs
 * library
 *
 * Pluggable Resources Set is a small library for simplifying plugin based
 * applications
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
package net.thevpc.common.prs.artset;

import java.io.File;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Properties;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com) %creationtime Dec 15, 2007
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
