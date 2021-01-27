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

import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;
import net.thevpc.common.prs.log.TLogStringBuffer;
import net.thevpc.common.prs.log.TLoadableLog;
import net.thevpc.common.prs.Version;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 30 dec. 2006 21:15:49
 */
public class PluginDescriptor implements Comparable<PluginDescriptor>, Serializable {

    public static enum Status {

        INSTALLED, INSTALLABLE, INAPPROPRIATE, UPDATABLE, OBSOLETE, FOR_INSTALL, FOR_UNINSTALL
    }
    private boolean system;
    private String className;
    private String CRC;
    private String id;
    private String defaultId;
    private String title;
    private VersionInterval applicationVersion;
    private Version version;
    private String description;
    private PluginDependency[] dependencies = new PluginDependency[0];
    private String[] reverseDependencies = new String[0];
    private String author;
    private String category;
    private String contributors;
    private String homeUrl;
    private URL pluginURL;
    private boolean loaded = true;
    private boolean valid = true;
    private URL[] extendedResources;
    private URL[] allResources;
    private boolean forUninstall;
    private String sourceUrl;
    private String binaryUrl;
    private long binarySize;
    private long sourceSize;
    private boolean dynamicLoading = true;
    private URL[] resources;
    private Set<String> interfaces;
    private Map<String, Set<String>> implementations;
    private Map<String, Set<String>> implementationsFactories;
//    private transient ClassLoader parentClassLoader;
    private transient ClassLoader classLoader;
    private transient SoftClassLoader softClassLoader;
    private transient UrlCacheManager urlCacheManager;
    //private transient List<ClassLoader> dependencyClassLoaders = new ArrayList<ClassLoader>();
    private transient TLoadableLog log = new TLogStringBuffer();
    private transient PluginRepository repository;
    private transient Status status = Status.INSTALLABLE;
    private transient PropertyChangeSupport support;
    /**
     * true if the plugin has been updated externally (new version or invalid CRC)
     */
    private boolean updated;
    private File workingDirectory = null;

    /**
     * Default Constructor, needed for Reflection (XmlDeserializer)
     */
    public PluginDescriptor() {
        support = new PropertyChangeSupport(this);
    }

    /**
     * @param className         class name
     * @param pluginURL         plugin full url
     * @param system            true if system plugin (core)
     */
    public PluginDescriptor(String className, URL pluginURL, boolean system) {
        this();
        this.className = className;
//        this.parentClassLoader = parentClassLoader;
        this.pluginURL = pluginURL;
        this.extendedResources = new URL[0];
        this.system = system;
    }

    public void setLog(TLoadableLog log) {
        this.log = log;
    }

    public boolean isSystem() {
        return system;
    }

    public URL[] getAllResources() {
        return allResources;
    }

    public void addResources(PluginDescriptor other) {
        //addDependencyClassLoader(other.getClassLoader());
        //addResources(other.getBaseResources());
    }

    public URL[] getResources() {
        return resources;
    }

    public void setResources(URL[] resources) {
        this.resources = resources;
    }

    public String getPluginClassName() {
        return className;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public SoftClassLoader getSoftClassLoader() {
        return softClassLoader;
    }

    public void setSoftClassLoader(SoftClassLoader softClassLoader) {
        this.softClassLoader = softClassLoader;
    }

    public URL getPluginURL() {
        return pluginURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String old = this.title;
        this.title = trim(title);
        support.firePropertyChange("title", old, title);
    }

    public Version getVersion() {
        return version;
    }

//    public String getMinorVersion() {
//        if (version == null || version.indexOf('.') < 0) {
//            return "0";
//        }
//        return version.substring(version.indexOf('.') + 1);
//    }
//
//    public String getMajorVersion() {
//        if (version == null) {
//            return "1";
//        }
//        if (version.indexOf('.') < 0) {
//            return version;
//        }
//        return version.substring(0, version.indexOf('.'));
    //    }
    public void setVersion(Version version) {
        Version old = this.version;
        this.version = version;
        support.firePropertyChange("version", old, version);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String old = this.description;
        this.description = trim(description);
        support.firePropertyChange("description", old, description);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        String old = this.author;
        this.author = trim(author);
        support.firePropertyChange("author", old, author);
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        String old = this.homeUrl;
        this.homeUrl = trim(homeUrl);
        support.firePropertyChange("homeUrl", old, homeUrl);
    }

    private String trim(String v) {
        if (v != null) {
            v = v.trim();
        }
        return (v == null || v.length() == 0) ? null : v;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        String old = this.contributors;
        this.contributors = contributors;
        support.firePropertyChange("contributors", old, contributors);
    }

    public String getCategory() {
        return category;
    }

    /**
     * Plugin Identifier
     * @return Plugin Identifier
     */
    public String getId() {
        return id == null ? getDefaultId() : id;
    }

    /**
     * A unique identifier that identifies this plugin descriptor according to id, version and CRC
     * Actually it corresponds to 
     * <pre>
     * getId() + "_" + getVersion() + "_" + getCRC()
     * </pre>
     * @return current UUID
     */
    public String getUUID() {
        return getId() + "_" + getVersion() + "_" + getCRC();
    }

    public void setId(String id) {
        if (id != null) {
            id = id.trim();
            if (id.length() == 0) {
                id = null;
            }
        }
        this.id = id;

        defaultId = null;
    }

    public String getDefaultId() {
        if (defaultId == null) {
            String f = pluginURL.getFile();
            int x = f.lastIndexOf('.');
            int y = f.lastIndexOf('/');
            defaultId = f.substring(y + 1, x);
        }
        return defaultId;
    }

    public void setCategory(String category) {
        String old = this.category;
        this.category = trim(category);
        support.firePropertyChange("category", old, category);
    }

    public PluginDependency getDependency(String id) {
        if (dependencies == null || id == null) {
            return null;
        }
        for (PluginDependency pluginDependency : dependencies) {
            if (id.equals(pluginDependency.getId())) {
                return pluginDependency;
            }
        }
        return null;
    }

    public PluginDependency[] getDependencies() {
        return dependencies;
    }

    /**
     * @return plugins that depends on this plugin
     */
    public String[] getReverseDependencies() {
        return reverseDependencies;
    }

    public void setReverseDependencies(String[] reverseDependencies) {
        this.reverseDependencies = null;
        addReverseDependencies(reverseDependencies);
    }

    public void setDependencies(PluginDependency[] dependencies) {
        this.dependencies = null;
        addDependencies(dependencies);
    }

    public void addDependencies(PluginDependency... dependencies) {
        PluginDependency[] oldDeps = this.dependencies;
        ArrayList<PluginDependency> old = new ArrayList<PluginDependency>();
        if (this.dependencies != null) {
            old.addAll(Arrays.asList(this.dependencies));
        }
        if (dependencies != null) {
            for (PluginDependency newDep : dependencies) {
                if (newDep != null) {
                    old.add(newDep);
                }
            }
        }
        this.dependencies = old.toArray(new PluginDependency[old.size()]);
        support.firePropertyChange("dependencies", oldDeps, dependencies);
    }

    public void addReverseDependencies(String... dependencies) {
        String[] oldDeps = this.reverseDependencies;
        ArrayList<String> old = new ArrayList<String>();
        if (this.reverseDependencies != null) {
            old.addAll(Arrays.asList(this.reverseDependencies));
        }
        if (dependencies != null) {
            for (String newDep : dependencies) {
                if (newDep != null) {
                    old.add(newDep);
                }
            }
        }
        this.reverseDependencies = old.toArray(new String[old.size()]);
        support.firePropertyChange("reverseDependencies", oldDeps, dependencies);
    }

    public String getPluginClassPath() {

        //String ps = System.getProperty("path.separator");
        StringBuilder sb = new StringBuilder();
        for (URL url : getPluginAndLibsURLs()) {
            if (sb.length() > 0) {
                sb.append(File.pathSeparator);
            }
            sb.append(url.getFile());
        }
        return sb.toString();
    }

    private static boolean isFileURL(URL repositoryURL) {
        return "file".equalsIgnoreCase(repositoryURL.getProtocol());
    }

    public TLoadableLog getLog() {
        return log;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        boolean old = this.valid;
        this.valid = valid;
        support.firePropertyChange("valid", old, valid);
    }

    public boolean isLoaded() {
        return valid;
    }

    public void setLoaded(boolean valid) {
        boolean old = this.valid;
        this.valid = valid;
        support.firePropertyChange("loaded", old, valid);
    }

    public boolean isForUninstall() {
        return forUninstall;
    }

    public void setForUninstall(boolean forUninstall) {
        boolean old = this.forUninstall;
        this.forUninstall = forUninstall;
        support.firePropertyChange("forUninstall", old, forUninstall);
    }

    public Collection<URL> findURL(UrlFilter urlFilter) throws IOException {
        URL mainUrl = getPluginURL();
        URLClassLoader ucl2 = new URLClassLoader(new URL[]{mainUrl}, null);
        JarInputStream jar = new JarInputStream(mainUrl.openStream());
        ZipEntry nextEntry;
        ArrayList<URL> found = new ArrayList<URL>();
        while ((nextEntry = jar.getNextEntry()) != null) {
            String path = nextEntry.getName();
//            String pathlc = path.toLowerCase();
            URL url = ucl2.getResource(path);
            if (urlFilter == null || urlFilter.accept(url, path)) {
                found.add(url);
            }
        }
        return found;
    }

    //    private static String getURLPath(URL repositoryURL) {
//        if ("file".equalsIgnoreCase(repositoryURL.getProtocol())) {
//            File folder = new File(repositoryURL.getFile());
//            try {
//                return (folder.getCanonicalPath());
//            } catch (IOException e) {
//                return (folder.getAbsolutePath());
//            }
//        } else {
//            return (repositoryURL.toString());
//        }
    //    }
    public long getBinarySize() {
        return binarySize;
    }

    public void setBinarySize(long binarySize) {
        long old = this.binarySize;
        this.binarySize = binarySize;
        support.firePropertyChange("binarySize", old, binarySize);
    }

    public String getBinaryUrl() {
        return binaryUrl;
    }

    public void setBinaryUrl(String binaryUrl) {
        String old = this.binaryUrl;
        this.binaryUrl = binaryUrl;
        support.firePropertyChange("binaryUrl", old, binaryUrl);
    }

    public long getSourceSize() {
        return sourceSize;
    }

    public void setSourceSize(long sourceSize) {
        long old = this.sourceSize;
        this.sourceSize = sourceSize;
        support.firePropertyChange("sourceSize", old, sourceSize);
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        String old = this.sourceUrl;
        this.sourceUrl = sourceUrl;
        support.firePropertyChange("sourceUrl", old, sourceUrl);
    }

    public String getAbsoluteBinaryUrl() {
        if (binaryUrl == null) {
            return null;
        }
        if (getRepository() != null) {
            if (binaryUrl.indexOf(':') > 0) {
                return binaryUrl;
            } else {
                return getRepository().getURL().toString() + "/" + binaryUrl;
            }
        } else {
            return binaryUrl;
        }
    }

    public String getAbsoluteSourceUrl() {
        if (sourceUrl == null) {
            return null;
        }
        if (getRepository() != null) {
            if (sourceUrl.indexOf(':') > 0) {
                return sourceUrl;
            } else {
                return getRepository().getURL().toString() + "/" + sourceUrl;
            }
        } else {
            return sourceUrl;
        }
    }

    public PluginRepository getRepository() {
        return repository;
    }

    public void setRepository(PluginRepository repository) {
        PluginRepository old = this.repository;
        this.repository = repository;
        support.firePropertyChange("repository", old, repository);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        Status old = this.status;
        this.status = status;
        support.firePropertyChange("status", old, status);
    }

    public int compareTo(PluginDescriptor o) {
        if (o == null) {
            return 1;
        }
        int x = this.id.compareTo(o.id);
        if (x == 0) {
            x = this.version.compareTo(o.version);
        }
        return x;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PluginDescriptor) {
            PluginDescriptor i = (PluginDescriptor) obj;
            return this.id.equals(i.getId()) && this.version.equals(i.getVersion());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.version != null ? this.version.hashCode() : 0);
        return hash;
    }

    public VersionInterval getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(VersionInterval applicationVersion) {
        VersionInterval old = this.applicationVersion;
        this.applicationVersion = applicationVersion;
        support.firePropertyChange("applicationVersion", old, applicationVersion);
    }

    @Override
    public String toString() {
        return "PluginInfo{"
                + "id='" + id + '\''
                + ", class=" + className
                + ", version=" + version
                + ", system=" + system
                + ", status=" + status
                + '}';
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }

    public boolean isDynamicLoading() {
        return dynamicLoading;
    }

    public void setDynamicLoading(boolean dynamicLoading) {
        this.dynamicLoading = dynamicLoading;
    }

//    public void addDependencyClassLoader(ClassLoader classLoader) {
//        //dependencyClassLoaders.add(classLoader);
//        this.classLoader = null;
//    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public UrlCacheManager getUrlCacheManager() {
        return urlCacheManager;
    }

    public void setUrlCacheManager(UrlCacheManager urlCacheManager) {
        this.urlCacheManager = urlCacheManager;
    }

    public String getCRC() {
        return CRC;
    }

    public void setCRC(String CRC) {
        this.CRC = CRC;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public URL[] getPluginAndLibsURLs() {
        ArrayList<URL> all = new ArrayList<URL>();
        all.add(getPluginURL());
        for (URL url : getResources()) {
            //TODO : should do some extra check on file extension??
            all.add(url);
        }
        return all.toArray(new URL[all.size()]);
    }

    public Set<String> getImplementations(String contract) {
        if (implementations != null) {
            Set<String> impls = implementations.get(contract);
            if (impls != null) {
                return impls;
            }
        }
        return Collections.EMPTY_SET;
    }

    public void setImplementations(Map<String, Set<String>> implementations) {
        this.implementations = implementations;
    }

    public Set<String> getImplementationFactories(String contract) {
        if (implementationsFactories != null) {
            Set<String> impls = implementationsFactories.get(contract);
            if (impls != null) {
                return impls;
            }
        }
        return Collections.EMPTY_SET;
    }

    public void setImplementationFactories(Map<String, Set<String>> implementations) {
        this.implementationsFactories = implementations;
    }

    public Set<String> getInterfaces() {
        return interfaces == null ? Collections.EMPTY_SET : interfaces;
    }

    public void setInterfaces(Set<String> interfaces) {
        this.interfaces = interfaces;
    }
}
