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
package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.classloader.ProxyClassLoader;
import net.thevpc.common.prs.factory.ExtensionDescriptor;
import net.thevpc.common.prs.factory.ImplementationDescriptor;
import net.thevpc.common.prs.factory.ImplementationFactoryDescriptor;
import net.thevpc.common.prs.reflect.FieldFilter;
import net.thevpc.common.prs.reflect.MethodFilter;
import net.thevpc.common.prs.reflect.Reflector;
import net.thevpc.common.prs.softreflect.classloader.HostToSoftClassLoader;
import net.thevpc.common.prs.softreflect.classloader.MultiSoftClassLoader;
import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;
import net.thevpc.common.prs.softreflect.classloader.URLSoftClassLoader;
import net.thevpc.common.prs.softreflect.SoftField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.thevpc.common.prs.softreflect.SoftReflector;
import net.thevpc.common.prs.softreflect.SoftAnnotation;
import net.thevpc.common.prs.softreflect.SoftAnnotationAttribute;
import net.thevpc.common.prs.softreflect.SoftClass;
import net.thevpc.common.prs.softreflect.SoftClassFilter;
import net.thevpc.common.prs.softreflect.SoftFieldFilter;
import net.thevpc.common.prs.softreflect.SoftMethod;
import net.thevpc.common.prs.softreflect.SoftMethodFilter;
import net.thevpc.common.prs.softreflect.SoftFieldImpl;
import net.thevpc.common.prs.softreflect.SoftMethodImpl;
import net.thevpc.common.prs.util.PRSPrivateIOUtils;
import net.thevpc.common.prs.util.ProgressMonitor;
import net.thevpc.common.prs.util.ProgressMonitorUtils;
import net.thevpc.common.prs.Version;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 15 nov. 2006 23:39:30
 */
public abstract class DefaultPluginManager<App extends PluggableApplication, Plug extends Plugin> implements PluginManager<App, Plug> {

    private App application;
    private PluginManagerCache cache;
//    private Logger log = Logger.getLogger(getClass().getName());
    private Version applicationVersion;
    private LinkedHashMap<String, Plug> plugins = new LinkedHashMap<String, Plug>();
    private LinkedHashMap<String, PluginDescriptor> pluginDescriptors = new LinkedHashMap<String, PluginDescriptor>();
    private TreeSet<PluginRepository> pluginRepositories = new TreeSet<PluginRepository>();
    private LocalRepository localRepository;
    //    private UrlCacheManager urlCacheManager;
    private Plug[] systemPlugins;
    private boolean safeMode;
    private PropertyChangeListener enabledPropertyChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            //noinspection unchecked
            Plug pp = (Plug) evt.getSource();
            if (!pp.getDescriptor().isSystem()) {
                localRepository.getPlugin(pp.getId()).setEnabled(pp.isEnabled());
            }
        }
    };
    private PluginLoader pluginLoader = new PluginLoader();
    private SoftClassFilter extensionFilter = new SoftClassFilter() {

        public boolean accept(SoftClass clz) {
            //noinspection unchecked
            if (!(clz.isInterface() || Modifier.isAbstract(clz.getModifiers()))) {
                return false;
            }
            for (SoftAnnotation ann : clz.getAnnotations()) {
                if (ann.getName().equals(Ignore.class.getName())) {
                    return false;
                }
                if (ann.getName().equals(Extension.class.getName())) {
                    return true;
                }
            }
            return false;
        }
    };
    private SoftClassFilter ignoreImplementationFilter = new SoftClassFilter() {

        public boolean accept(SoftClass clz) {
            for (SoftAnnotation ann : clz.getAnnotations()) {
                if (ann.getName().equals(Ignore.class.getName())) {
                    return true;
                }
            }
            return false;
        }
    };
    private SoftMethodFilter lifeCycleMethodsFilter = new SoftMethodFilter() {

        public boolean accept(SoftMethod method) {
            for (SoftAnnotation ann : method.getAnnotations()) {
                if (ann.getName().equals(Initializer.class.getName())) {
                    return true;
                }
            }
            return false;
        }
    };
    private SoftFieldFilter injectionFieldsFilter = new SoftFieldFilter() {

        public boolean accept(SoftField field) {
            for (SoftAnnotation ann : field.getAnnotations()) {
                if (ann.getName().equals(Inject.class.getName())) {
                    return true;
                }
                if (ann.getName().equals(NewInstance.class.getName())) {
                    return true;
                }
            }
            return false;
        }
    };
    @SuppressWarnings({"unchecked"})
    private List<URL> coreURLs = Collections.EMPTY_LIST;
    private ClassLoader coreClassLoader = getClass().getClassLoader();
    private SoftClassLoader softCoreClassLoader;
    private Comparator<PluginManagerCache.ImplementationCache> priorityComparator = new ImplementationCachePriorityComparator(this);
    private ExtensionDescriptor[] extensions;
    private Logger logger;

    public DefaultPluginManager() {
        pluginLoader = new PluginLoader();
        pluginLoader.addListener(new DefaultPluginLoaderListener(this));
    }

    public void addPluginRepository(PluginRepository r) {
        pluginRepositories.add(r);
    }

    public void removePluginRepository(PluginRepository r) {
        pluginRepositories.add(r);
    }

    public PluginRepository[] getPluginRepositories() {
        return pluginRepositories.toArray(new PluginRepository[pluginRepositories.size()]);
    }

    public PluginDescriptor[] getAvailablePluginDescriptors() {
        TreeSet<PluginDescriptor> found = new TreeSet<PluginDescriptor>();
        TreeSet<String> forInstall = new TreeSet<String>(Arrays.asList(getLocalRepository().getPluginsForInstall()));
        TreeSet<String> forUninstall = new TreeSet<String>(Arrays.asList(getLocalRepository().getPluginsForUninstall()));
        for (PluginRepository pluginRepository : getPluginRepositories()) {
            for (Iterator<PluginDescriptor> i = pluginRepository.getPluginDescriptors(); i.hasNext();) {
                PluginDescriptor descriptor = i.next();
                if (!found.contains(descriptor)) {
                    if (forInstall.contains(descriptor.getId())) {
                        descriptor.setStatus(PluginDescriptor.Status.FOR_INSTALL);
                    } else if (forUninstall.contains(descriptor.getId())) {
                        descriptor.setStatus(PluginDescriptor.Status.FOR_UNINSTALL);
                    } else {
                        Plug plug = getPlugin(descriptor.getId());
                        if (plug == null) {
                            VersionInterval appVersion = descriptor.getApplicationVersion();
                            int cmp = appVersion == null ? 0 : appVersion.compare(applicationVersion);
                            if (cmp == 0) {
                                descriptor.setStatus(PluginDescriptor.Status.INSTALLABLE);
                            } else {
                                descriptor.setStatus(PluginDescriptor.Status.INAPPROPRIATE);
                            }
                        } else {
                            int comp = plug.getDescriptor().getVersion().compareTo(descriptor.getVersion());
                            if (comp == 0) {
                                descriptor.setStatus(PluginDescriptor.Status.INSTALLED);
                            } else if (comp < 0) {
                                descriptor.setStatus(PluginDescriptor.Status.UPDATABLE);
                            } else {
                                //System.err.println("Plugin " + pluginInfo.getId() + " OBSOLETE PluginVersion(available=" + pluginInfo.getVersion() + "/current=" + plug.getPluginInfo().getVersion() + ");AplpicationVersion(needed=" + pluginInfo.getApplicationVersion() + "/current=" + getApplicationVersion() + ")");
                                descriptor.setStatus(PluginDescriptor.Status.OBSOLETE);
                            }
                        }
                    }
                    boolean shouldAdd = true;
                    //if there is a version
                    for (Iterator<PluginDescriptor> ii = found.iterator(); ii.hasNext();) {
                        PluginDescriptor info = ii.next();
                        if (info.getId().equals(descriptor.getId()) && info.getStatus().equals(descriptor.getStatus())) {
                            int comp = descriptor.getVersion().compareTo(info.getVersion());
                            if (comp > 0) {
                                ii.remove();
                            } else if (comp < 0) {
                                shouldAdd = false;
                            } else /*(comp==0)*/ {
                                shouldAdd = false;
                            }
                        }
                    }
                    if (shouldAdd) {
                        found.add(descriptor);
                    }
                }
            }
        }
        return found.toArray(new PluginDescriptor[found.size()]);
    }

    public Plug[] getSystemPlugins() {
        if (systemPlugins == null) {
            ArrayList<Plug> all = new ArrayList<Plug>();
            for (Plug plug : plugins.values()) {
                if (plug.getDescriptor().isSystem()) {
                    all.add(plug);
                }
            }
            systemPlugins = toPluginsArray(all.toArray(new Plugin[all.size()]));
        }
        return systemPlugins;
    }

    public void init(App app, Version applicationVersion) throws PluginException {
        this.application = app;
        this.applicationVersion = applicationVersion;
        logger = application.getLogger(DefaultPluginManager.class.getName());
    }

    protected void buildStart(ProgressMonitor monitor) throws PluginException {
    }
    
    protected void buildEnd(ProgressMonitor monitor) throws PluginException {
    }

    protected void buildPostPrepare(ProgressMonitor monitor) throws PluginException {
    }

    protected void buildPostLookupPlugins(ProgressMonitor monitor) throws PluginException {
    }

    protected void buildPostLookupExtensions(ProgressMonitor monitor) throws PluginException {
    }

    protected void buildPostCreatePlugins(ProgressMonitor monitor) throws PluginException {
    }

    protected void buildPrepare(ProgressMonitor monitor) throws PluginException {
        try {
            localRepository.load();
        } catch (PluginException e) {
            logger.log(Level.SEVERE, "Load local repository failed", e);
        }
        try {
            registerPlugins();
        } catch (PluginException e) {
            logger.log(Level.SEVERE, "registerPlugins failed", e);
        }
    }

    public void close() throws PluginException {
        logger.log(Level.FINE, "dispose and unregister plugins");
        Plugin[] allPlugins = getAllPlugins();
        for (int i = allPlugins.length - 1; i >= 0; i--) {
            Plugin plugin = allPlugins[i];
            if (plugin.isInitialized()) {
                try {
                    plugin.applicationClosing();
                } catch (Throwable e) {
                    //????
                    //getMessageDialogManager().showMessage(null, null, MessageDialogType.ERROR,null, e);
                    logger.log(Level.SEVERE, "applicationClosing failed", e);
                    //                getMessageDialogManager().showMessage(null, null, MessageDialogType.ERROR,null, e);
                }
            }
        }
        try {
            unregisterAvailablePlugins();
        } catch (PluginException e) {
            //getMessageDialogManager().showMessage(null, null, MessageDialogType.ERROR,null, e);
            logger.log(Level.SEVERE, "unregisterAvailablePlugins failed", e);
        }
        localRepository.store();
        //getUrlCacheManager().clearCacheFolder();
    }

    public void registerPlugins() throws PluginException {

//        URL repositoryURL = getInstalledPluginsRepositoryURL();
        //        ClassLoader parent = getClass().getClassLoader();
        URL[] pluginUrls = getLocalRepository().getPluginURLs();
        Arrays.sort(pluginUrls, new Comparator<URL>() {

            public int compare(URL o1, URL o2) {
                String a1 = o1.toString();
                String a2 = o2.toString();
                return a1.compareTo(a2);
            }
        });
        LinkedHashMap<String, PluginDescriptor> allDescs = new LinkedHashMap<String, PluginDescriptor>();
        for (final URL url : pluginUrls) {
            PluginDescriptor descriptor = loadPluginDescriptor(url);
            if (descriptor != null) {
                if (allDescs.containsKey(descriptor.getId())) {
                    PluginDescriptor other = allDescs.get(descriptor.getId());
                    logger.log(Level.SEVERE, "unable to register " + "plugin {0}" + ", ignored  : Already registered" + "\n\tNew plugin is {1}, class {2}, url {3}\n\tExisting plugin is {4}, class {5}, url {6}", new Object[]{descriptor.getId(), descriptor.getId(), descriptor.getPluginClassName(), descriptor.getPluginURL(), other.getId(), other.getPluginClassName(), other.getPluginURL()});
                } else {
                    allDescs.put(descriptor.getId(), descriptor);
                }
            }
        }
        registerPlugins(allDescs.values().toArray(new PluginDescriptor[allDescs.size()]));
    }

    @Override
    public void installPlugin(URL newPluginUrl, boolean installDependencies) throws PluginException {
        try {
            URL localUrl = localRepository.addInstallable(newPluginUrl.openStream(), PRSPrivateIOUtils.getFileNameWithoutExtension(new File(newPluginUrl.getFile())));
            PluginDescriptor pluginDescriptor = loadPluginDescriptor(localUrl);
            if (installDependencies && pluginDescriptor != null) {
                for (PluginDependency s : pluginDescriptor.getDependencies()) {
                    installPlugin(s.getId(), s.getVersionInterval(), installDependencies);
                }
            }
        } catch (IOException e) {
            throw new PluginException(newPluginUrl.toString(), e);
        }
    }

    public void installPlugin(String plugin, VersionInterval ve, boolean installDependencies) throws PluginException {
        PluginDescriptor descriptor = null;
        for (PluginDescriptor anyDescriptor : getAvailablePluginDescriptors()) {
            if (anyDescriptor.getId().equals(plugin)) {
                descriptor = anyDescriptor;
                break;
            }
        }
        if (descriptor == null) {
            throw new PluginException(plugin, "Plugin " + plugin + " not found");
        }
        if (descriptor.getStatus().equals(PluginDescriptor.Status.INSTALLABLE) || descriptor.getStatus().equals(PluginDescriptor.Status.UPDATABLE)) {
            try {
                URL url = new URL(descriptor.getAbsoluteBinaryUrl());
                try {
                    String fileName = PRSPrivateIOUtils.getFileNameWithoutExtension(new File(url.getFile()));
                    ProgressMonitorInputStream2 in = new ProgressMonitorInputStream2(null, "loading " + fileName + "...", url.openStream(), descriptor.getBinarySize());
                    localRepository.addInstallable(in, fileName);
                    if (installDependencies) {
                        for (PluginDependency s : descriptor.getDependencies()) {
                            installPlugin(s.getId(), s.getVersionInterval(), installDependencies);
                        }
                    }
                } catch (IOException e) {
                    throw new PluginException(url.toString(), e);
                }
            } catch (MalformedURLException e) {
                throw new PluginException(plugin, "Plugin plugin not found : " + e.toString());
            }
        }
    }

    public void uninstallPlugin(String pluginId) throws PluginException {
        Plug plug = getPlugin(pluginId);
        PluginDescriptor descriptor = plug.getDescriptor();
        if (!descriptor.isSystem() && !descriptor.isForUninstall()) {
            localRepository.markPluginForUninstall(descriptor.getId());
            descriptor.setForUninstall(true);
        } else {
            throw new PluginException(pluginId, "Could not uninstall System or Pending Install/Uninstall Plugins");
        }
    }

    public PluginDescriptor loadPluginDescriptor(final URL url) throws PluginException {
        return loadPluginDescriptor(url, getClass().getClassLoader());
    }

    public File getPluginVarFolder(String pluginId) throws PluginException {
        return new File("plugins", pluginId);
    }

    public final PluginDescriptor loadPluginDescriptor(final URL url, final ClassLoader parent) throws PluginException {
        return pluginLoader.loadPluginDescriptor(url, parent);
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    private boolean fillPluginLoaderGroup(Map<String, PluginDescriptor> all, String id, List<String> group) throws PluginException {
        if (group.contains(id)) {
            return false;
        }
        group.add(id);
        PluginDescriptor descriptor = all.get(id);
        if (descriptor == null) {
            throw new PluginException(id, "Plugin " + id + " not found. Dependency checking failed.");
        }
        PluginDependency[] dependencies = descriptor.getDependencies();
        boolean ok = false;
        for (PluginDependency dependency : dependencies) {
            if (fillPluginLoaderGroup(all, dependency.getId(), group)) {
                ok = true;
            }
        }
        return ok;
    }

    public void unregisterAvailablePlugins() throws PluginException {
        logger.log(Level.FINE, "unregister plugins");
        for (Plug plugin : plugins.values()) {
            plugin.applicationClosing();
        }
    }

    @Override
    public synchronized boolean registerPlugins(PluginDescriptor... descriptors) throws InvalidPluginException {
        logger.log(Level.FINE, "register plugins ", descriptors);
        boolean success = true;
        for (PluginDescriptor descriptor : descriptors) {
            if (descriptor != null) {
                String id = descriptor.getId();
                if (this.pluginDescriptors.containsKey(id)) {
                    int counter = 2;
                    while (this.pluginDescriptors.containsKey(id + counter)) {
                        counter++;
                    }
                    descriptor.setId(id + counter);
                    descriptor.setLoaded(false);
                    descriptor.getLog().warning("Plugin " + id + " already registered, renamed to " + descriptor.getId());
                    success = false;
                }
                this.pluginDescriptors.put(descriptor.getId(), descriptor);
            }
        }
        for (PluginDescriptor info : descriptors) {
            if (info != null) {
                try {
                    ArrayList<String> group = new ArrayList<String>();
                    fillPluginLoaderGroup(this.pluginDescriptors, info.getId(), group);
                    if (group.size() > 1) {
                        PluginDescriptor first = this.pluginDescriptors.get(info.getId());
                        for (String s : group) {
                            PluginDescriptor second = this.pluginDescriptors.get(s);
                            first.addResources(second);
                        }
                    }
                    info.getLog().trace("Plugin " + info.getId() + " registered successfully");
                } catch (Throwable e) {
                    info.getLog().error("Unable to register plugin " + info.getPluginClassName() + " , ignored" + ". url : " + info.getPluginURL(), e);
                    //e.printStackTrace();
                    info.setLoaded(false);
                    success = false;
                }
            }
        }
        //recalculate ReverseDependencies
        for (PluginDescriptor p : this.pluginDescriptors.values()) {
            p.setReverseDependencies(null);
        }
        for (PluginDescriptor p : this.pluginDescriptors.values()) {
            for (PluginDependency pd : p.getDependencies()) {
                PluginDescriptor rplug = this.pluginDescriptors.get(pd.getId());
                if (rplug != null) {
                    rplug.addReverseDependencies(p.getId());
                }
            }
        }
//        //TODO : this is a classloading workaround, should do in a different way
//        ArrayList<URL> urls = new ArrayList<URL>();
//        for (PluginDescriptor p : this.pluginDescriptors.values()) {
//            urls.add(p.getPluginURL());
//            urls.addAll(Arrays.asList(p.getResources()));
//        }
//        try {
//            PluggableResourcesClassLoader cl = new PluggableResourcesClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader(), getClass().getSimpleName());
//            for (PluginDescriptor p : this.pluginDescriptors.values()) {
//                p.setPluginClassLoader(cl);
//            }
//        } catch (IOException e) {
//            throw new InvalidPluginException(null, e);
//        }

        for (PluginDescriptor descriptor : descriptors) {
            boolean valid = false;
            try {
                checkValidPlugin(descriptor.getId());
                valid = true;
            } catch (Exception e) {
                descriptor.getLog().error(e.toString());
            }
            descriptor.setValid(valid);
        }
        Map<String, ClassLoader> hardClassLoaders = buildPluginsHardClassLoaders();
        for (Map.Entry<String, ClassLoader> entry : hardClassLoaders.entrySet()) {
            String pluginId = entry.getKey();
            if (pluginId != null) {
                getPluginDescriptor(pluginId).setClassLoader(entry.getValue());
            }
        }

        systemPlugins = null;
        return success;
    }

    @Override
    public void unregisterPlugin(String pluginId) throws PluginException {
        if (pluginId == null) {
            throw new InvalidPluginException("Null Plugin to register");
        }
        try {
            Plug plugin = getPlugin(pluginId);
            plugin.applicationClosing();
            plugin.pluginUnregistered();
            plugins.remove(pluginId);
            systemPlugins = null;
        } catch (Throwable e) {
            throw new InvalidPluginException("Unable to unregister plugin " + pluginId, e);
        }
    }

    @Override
    public Plug[] getEnabledPlugins() {
        ArrayList<Plug> valid = new ArrayList<Plug>();
        for (Plug dbcPlugin : plugins.values()) {
            if (dbcPlugin.isEnabled()) {
                valid.add(dbcPlugin);
            }
        }
        return toPluginsArray(valid.toArray());
    }

    public void buildCreatePlugins(ProgressMonitor monitor) {
        logger.log(Level.FINE, "Create Plugins");
        for (PluginDescriptor descriptor : pluginDescriptors.values()) {
            Plug plugin = null;
            try {
                plugin = createPlugin(descriptor);
                plugin.getLogger(DefaultPluginManager.class.getName()).log(Level.CONFIG, "Plugin {0}-{1} created", new Object[]{descriptor.getId(), descriptor.getVersion(), plugin.getClass().getName()});
                String pid = descriptor.getId();
                PluginRepositoryInfo ii = null;
                try {
                    ii = localRepository.getPlugin(pid);
                } catch (NoSuchElementException e) {
                    //do nothing
                }
                if (ii != null && !ii.isEnabled()) {
                    plugin.setEnabled(false);
                }
                plugins.put(pid, plugin);
                PluginRepositoryInfo repositoryInfo = new PluginRepositoryInfo(pid);
                repositoryInfo.setEnabled(plugin.isEnabled());
                //TODO r.register(repositoryInfo);
                plugin.pluginRegistered();
                plugin.getLogger(DefaultPluginManager.class.getName()).log(Level.CONFIG, "Plugin {0}-{1} registered successfully as {2}", new Object[]{descriptor.getId(), descriptor.getVersion(), plugin.getClass().getName()});
            } catch (Throwable e) {
                plugin.getLogger(DefaultPluginManager.class.getName()).log(Level.SEVERE, "Unable to register plugin " + descriptor.getPluginClassName() + " , ignored" + ". url : " + descriptor.getPluginURL(), e);
                if (plugin != null) {
                    plugin.setEnabled(false);
                    throw new InvalidPluginException("Unable to register plugin " + plugin, e);
                }
            }
        }
    }

    public abstract Class<Plug> getPluginClass();

    private Plug[] toPluginsArray(Object[] aa) {
        Class gd = getPluginClass();
        Object[] plugArr = (Object[]) Array.newInstance(gd, aa.length);
        System.arraycopy(aa, 0, plugArr, 0, aa.length);
        return (Plug[]) plugArr;
    }

    public Collection<Plug> getPluginsList() {
        return plugins.values();
    }

    public Plug[] getAllPlugins() {
        Collection<Plug> plugs = getPluginsList();
        return toPluginsArray(plugs.toArray());
    }

    public Plug[] getPlugins(PluginFilter<Plug> filter) {
        if (filter == null) {
            Collection<Plug> plugs = getPluginsList();
            return toPluginsArray(plugs.toArray());
        }
        ArrayList<Plug> plugs = new ArrayList<Plug>();
        for (Plugin plugin : plugins.values()) {
            Plug p = (Plug) plugin;
            if (filter.accept(p)) {
                plugs.add(p);
            }
        }
        return toPluginsArray(plugs.toArray());
    }

    public Plug getPlugin(String pluginId) {
        return plugins.get(pluginId);
    }

    public Plug getValidPlugin(String pluginId) {
        if (getPlugin(pluginId).getDescriptor().isValid()) {
            return getPlugin(pluginId);
        }
        return null;
    }

    public PluginDescriptor.Status getPluginStatus(String pluginId) {
        Plug plug = plugins.get(pluginId);
        if (plug != null) {
            return plug.getDescriptor().getStatus();
        }
        for (String s : getLocalRepository().getPluginsForInstall()) {
            if (s.equals(pluginId)) {
                return PluginDescriptor.Status.FOR_INSTALL;
            }
        }
        for (String s : getLocalRepository().getPluginsForUninstall()) {
            if (s.equals(pluginId)) {
                return PluginDescriptor.Status.FOR_UNINSTALL;
            }
        }
        return PluginDescriptor.Status.INSTALLABLE;
    }

    public boolean isPluginEnabled(String pluginId) {
        //        PluginRepositoryInfo repositoryInfo = null;
        try {
            return !isSafeMode() && localRepository.getPlugin(pluginId).isEnabled();
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    protected Plug createPlugin(PluginDescriptor descriptor) {
        Plug plugin = null;
        plugin = createPluginImpl(descriptor);
        plugin.setDescriptor(descriptor);
        plugin.setEnabled(descriptor.isValid() && (descriptor.isSystem() || isPluginEnabled(descriptor.getId())));
        plugin.addPropertyChangeListener("enabled", enabledPropertyChangeListener);
        plugin.init(application, this);
        return plugin;
    }

    protected abstract Plug createPluginImpl(PluginDescriptor descriptor);

    private static boolean isFileURL(URL repositoryURL) {
        return "file".equalsIgnoreCase(repositoryURL.getProtocol());
    }

    public App getApplication() {
        return application;
    }

//    public UrlCacheManager getUrlCacheManager() {
//        return urlCacheManager;
//    }
//
//    public void setUrlCacheManager(UrlCacheManager urlCacheManager) {
//        this.urlCacheManager = urlCacheManager;
//    }
    public Version getApplicationVersion() {
        return applicationVersion;
    }

    public boolean isSafeMode() {
        return safeMode;
    }

    /**
     * when safe mode new plugins will not be installed and all loaded plugions will be disabled
     *
     * @param safeMode true if no plugins hve to be loaded
     */
    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
    }

    public void checkValidPlugin(String plugin) {
        PluginDescriptor plug = pluginDescriptors.get(plugin);
        if (plug == null) {
            throw new PluginException(plugin, "Plugin " + plugin + " not found");
        }
        TreeSet<String> visited = new TreeSet<String>();
        checkValidPlugin(plug, plugin, visited);
    }

    private void checkValidPlugin(PluginDescriptor descriptor, String initialPlugin, TreeSet<String> visited) throws PluginException {
        if (descriptor == null) {
            throw new UnknownPluginException(null, "Plugin Not found");
        }
        if (!descriptor.isLoaded()) {
            throw new UnknownPluginException(descriptor.getId(), "Plugin not loaded");
        }
        if (descriptor.isSystem()) {
            return;
        }
        visited.add(descriptor.getId());
        for (PluginDependency rDep : descriptor.getDependencies()) {
            PluginDescriptor p = pluginDescriptors.get(rDep.getId());
            if (p != null) {
                if (visited.contains(p.getId())) {
                    throw new UnknownPluginException(initialPlugin, "Loop encountered in checking plugin validity " + initialPlugin + " : loop on " + p.getId());
                }
                try {
                    checkValidPlugin(p, initialPlugin, visited);
                } catch (PluginException e) {
                    throw new RequiredPluginException(initialPlugin, rDep.getId());
                }
            } else {
                //required dependency not found (not installed)
                throw new RequiredPluginException(initialPlugin, rDep.getId());
            }
        }
//        if (!descriptor.isEnabled()) {
//            throw new DisabledPluginException(descriptor.getId());
//        }
    }

    public Collection<PluginDescriptor> getPluginDescriptors() {
        return new ArrayList<PluginDescriptor>(pluginDescriptors.values());
    }

    public PluginDescriptor getPluginDescriptor(String id) {
        return pluginDescriptors.get(id);
    }

//    protected void setExtensionFilter(ClassFilter extensionFilter) {
//        this.extensionFilter = extensionFilter;
//    }
//
//    public void setInjectionFieldsFilter(FieldFilter injectionFieldsFilter) {
//        this.injectionFieldsFilter = injectionFieldsFilter;
//    }
//
//    public void setLifeCycleMethodsFilter(MethodFilter lifeCycleMethodsFilter) {
//        this.lifeCycleMethodsFilter = lifeCycleMethodsFilter;
//    }
//
//    public void setIgnoreImplementationFilter(ClassFilter ignoreImplementationFilter) {
//        this.ignoreImplementationFilter = ignoreImplementationFilter;
//    }
    protected String computeDescriptorsCRC() throws IOException {
        Collection<PluginDescriptor> pluginsInfosCollection = getPluginDescriptors();
        TreeSet<String> crcs = new TreeSet<String>();
        for (PluginDescriptor descriptor : pluginsInfosCollection) {
            crcs.add(descriptor.getUUID());
        }
        return PRSPrivateIOUtils.computeCRC(crcs.toString().getBytes());
    }

    protected String getDescriptorsCRC() {
        File pluginsCacheCRCFile = new File(getPluginsVarFolder(), "plugins.cache.nfo");
        if (pluginsCacheCRCFile.exists()) {
            try {
                return PRSPrivateIOUtils.loadString(pluginsCacheCRCFile);
            } catch (IOException e) {
                //ignore
            }
        }
        return null;
    }

    protected void setDescriptorsCRC(String CRC) {
        File pluginsCacheCRCFile = new File(getPluginsVarFolder(), "plugins.cache.nfo");
        try {
            pluginsCacheCRCFile.delete();
            PRSPrivateIOUtils.saveString(pluginsCacheCRCFile, CRC);
        } catch (IOException e) {
            //ignore
        }
    }

    protected void buildLookupPlugins(ProgressMonitor monitor) {
        boolean invalidateCache = false;
        File pluginsFolder = getPluginsVarFolder();
        logger.log(Level.FINE, "Lookup plugins from {0}", pluginsFolder.getPath());
        File pluginsCacheFile = new File(pluginsFolder, "plugins.cache");
        String CRC = null;
        try {
            CRC = computeDescriptorsCRC();
            String oldCRC = getDescriptorsCRC();
            invalidateCache = !CRC.equals(oldCRC);
        } catch (IOException e) {
            invalidateCache = true;
        }
        if (invalidateCache) {
            logger.log(Level.FINE, "Cache invalidated");
            cache = null;
        } else if (pluginsCacheFile.exists()) {
            try {
                cache = (PluginManagerCache) PRSPrivateIOUtils.loadObject(pluginsCacheFile);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to Load Cache to " + pluginsCacheFile.getPath(), e);
            }
        }
        if (cache == null) {
            cache = rebuildPluginManagerCache();
            pluginsCacheFile.delete();
            setDescriptorsCRC(CRC);
            try {
                PRSPrivateIOUtils.saveObject(pluginsCacheFile, cache);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Unable to Save Cache to " + pluginsCacheFile.getPath(), e);
            }
        }
        for (PluginDescriptor plugin : getPluginDescriptors()) {
            PluginManagerCache.PluginCache pc = getCache().getPluginCache(plugin.getId());
            if (pc != null) {
                Set<String> ii = new HashSet<String>(pc.getExtensions());
                Map<String, Set<String>> jj = new HashMap<String, Set<String>>();
                for (Map.Entry<String, Set<PluginManagerCache.ImplementationCache>> entry : pc.getImplementations().entrySet()) {
                    String inter = entry.getKey();
                    Set<String> set = new HashSet<String>();
                    for (PluginManagerCache.ImplementationCache c : entry.getValue()) {
                        set.add(c.getImplementationName());
                    }
                    jj.put(inter, set);
                }
                Map<String, Set<String>> hh = new HashMap<String, Set<String>>();
                for (Map.Entry<String, Set<PluginManagerCache.ImplementationCache>> entry : pc.getImplementationFactories().entrySet()) {
                    String inter = entry.getKey();
                    Set<String> set = new HashSet<String>();
                    for (PluginManagerCache.ImplementationCache c : entry.getValue()) {
                        set.add(c.getImplementationName());
                    }
                    hh.put(inter, set);
                }
                plugin.setInterfaces(ii);
                plugin.setImplementations(jj);
                plugin.setImplementationFactories(hh);
            }
        }

    }

    public SoftClassLoader getSoftCoreClassLoader() {
        if (softCoreClassLoader == null) {
            softCoreClassLoader = new HostToSoftClassLoader("<core>", getCoreClassLoader(), null);
        }
        return softCoreClassLoader;
    }

    protected Map<String, SoftClassLoader> buildPluginSoftClassLoaders() {
        logger.log(Level.FINE, "Building PLugins Soft ClassLoaders...");
        Collection<PluginDescriptor> descriptors = getPluginDescriptors();
        Map<String, SoftClassLoader> directClassLoaders = new HashMap<String, SoftClassLoader>();
        Map<String, SoftClassLoader> lightClassLoaders = new HashMap<String, SoftClassLoader>();
        final SoftClassLoader coreRClassLoader = getSoftCoreClassLoader();
        directClassLoaders.put(null, coreRClassLoader);
        for (PluginDescriptor descriptor : new ArrayList<PluginDescriptor>(descriptors)) {
            if (descriptor.isValid()) {
                directClassLoaders.put(descriptor.getId(), new URLSoftClassLoader(descriptor.getId(), descriptor.getPluginAndLibsURLs(), SoftReflector.getClassBuilder(), coreRClassLoader));
            } else {
                descriptors.remove(descriptor);
            }
        }
        boolean somethingDone = true;
        while (somethingDone) {
            somethingDone = false;
            for (PluginDescriptor descriptor : new ArrayList<PluginDescriptor>(descriptors)) {
                final PluginDependency[] deps = descriptor.getDependencies();
                SoftClassLoader immediateRCL = directClassLoaders.get(descriptor.getId());
                if (deps.length == 0) {
                    somethingDone = true;
                    lightClassLoaders.put(descriptor.getId(), immediateRCL);
                    descriptors.remove(descriptor);
                } else {
                    boolean depsOk = true;
                    for (PluginDependency pluginDependency : deps) {
                        if (!lightClassLoaders.containsKey(pluginDependency.getId())) {
                            depsOk = false;
                            break;
                        }
                    }
                    if (depsOk) {
                        ArrayList<SoftClassLoader> multiple = new ArrayList<SoftClassLoader>();
                        for (PluginDependency pluginDependency : deps) {
                            multiple.add(lightClassLoaders.get(pluginDependency.getId()));
                        }
                        somethingDone = true;
                        lightClassLoaders.put(descriptor.getId(), new MultiSoftClassLoader(descriptor.getId(), multiple.toArray(new SoftClassLoader[multiple.size()]), immediateRCL));
                        descriptors.remove(descriptor);
                    }
                }
            }
        }
        lightClassLoaders.put(null, coreRClassLoader);
        return lightClassLoaders;
    }

    protected Map<String, ClassLoader> buildPluginsHardClassLoaders() {
        logger.log(Level.FINE, "Building Plugins Hard ClassLoaders...");
        Map<String, ClassLoader> hardClassLoaders = new HashMap<String, ClassLoader>();
        Collection<PluginDescriptor> descriptors = getPluginDescriptors();
        for (PluginDescriptor descriptor : new ArrayList<PluginDescriptor>(descriptors)) {
            if (descriptor.isValid()) {
                buildPluginHardClassLoader(descriptor.getId(), hardClassLoaders);
            }
        }
        return hardClassLoaders;
    }

    private ClassLoader buildPluginHardClassLoader(String id, Map<String, ClassLoader> map) {
        ClassLoader aa = map.get(id);
        if (aa != null) {
            return aa;
        }
        List<ClassLoader> depClassLoaders = new ArrayList<ClassLoader>();
        PluginDescriptor pluginDescriptor = getPluginDescriptor(id);
        for (PluginDependency dep : pluginDescriptor.getDependencies()) {
            depClassLoaders.add(buildPluginHardClassLoader(dep.getId(), map));
        }
        final ClassLoader coreRClassLoader = ClassLoader.getSystemClassLoader();
//        depClassLoaders.add(new URLClassLoader(pluginDescriptor.getPluginAndLibsURLs(), coreRClassLoader));
        //MultiClassLoader c=new MultiClassLoader(coreClassLoader, cloaders)
        ClassLoader p = depClassLoaders.isEmpty() ? coreRClassLoader : new ProxyClassLoader(id+" Plugin ClassLoader",depClassLoaders.toArray(new ClassLoader[depClassLoaders.size()]));
        URLClassLoader pluginCL = new URLClassLoader(pluginDescriptor.getPluginAndLibsURLs(), p);
        map.put(id, pluginCL);
        return pluginCL;
    }

//    private Map<String, ClassLoader> buildPluginHardClassLoaders_depreciated() {
//        logger.log(Level.FINE, "Building Plugins Hard ClassLoaders...");
//        Collection<PluginDescriptor> descriptors = getPluginDescriptors();
//        Map<String, ClassLoader> directClassLoaders = new HashMap<String, ClassLoader>();
//        Map<String, ClassLoader> hardClassLoaders = new HashMap<String, ClassLoader>();
//        final ClassLoader coreRClassLoader = ClassLoader.getSystemClassLoader();
//        directClassLoaders.put(null, coreRClassLoader);
//        for (PluginDescriptor descriptor : new ArrayList<PluginDescriptor>(descriptors)) {
//            if (descriptor.isValid()) {
//                directClassLoaders.put(descriptor.getId(), new URLClassLoader(descriptor.getPluginAndLibsURLs(), coreRClassLoader));
//            } else {
//                descriptors.remove(descriptor);
//            }
//        }
//        boolean somethingDone = true;
//        while (somethingDone) {
//            somethingDone = false;
//            for (PluginDescriptor descriptor : new ArrayList<PluginDescriptor>(descriptors)) {
//                final PluginDependency[] deps = descriptor.getDependencies();
//                ClassLoader immediateRCL = directClassLoaders.get(descriptor.getId());
//                if (deps.length == 0) {
//                    somethingDone = true;
//                    hardClassLoaders.put(descriptor.getId(), immediateRCL);
//                    descriptors.remove(descriptor);
//                } else {
//                    boolean depsOk = true;
//                    for (PluginDependency pluginDependency : deps) {
//                        if (!hardClassLoaders.containsKey(pluginDependency.getId())) {
//                            depsOk = false;
//                            break;
//                        }
//                    }
//                    if (depsOk) {
//                        ArrayList<ClassLoader> multiple = new ArrayList<ClassLoader>();
//                        for (PluginDependency pluginDependency : deps) {
//                            multiple.add(hardClassLoaders.get(pluginDependency.getId()));
//                        }
//                        somethingDone = true;
//                        hardClassLoaders.put(descriptor.getId(), new MultiClassLoader(immediateRCL, multiple.toArray(new ClassLoader[multiple.size()])));
//                        descriptors.remove(descriptor);
//                    }
//                }
//            }
//        }
//        hardClassLoaders.put(null, coreRClassLoader);
//        return hardClassLoaders;
//    }
    private PluginManagerCache rebuildPluginManagerCache() {
        logger.log(Level.FINE, "Rebuild Plugins Cache...");
        PluginManagerCache pluginManagerCache = new PluginManagerCache();
        Collection<PluginDescriptor> descriptors = getPluginDescriptors();

        Map<String, SoftClassLoader> softClassLoaders = buildPluginSoftClassLoaders();
        for (Map.Entry<String, SoftClassLoader> entry : softClassLoaders.entrySet()) {
            String pluginId = entry.getKey();
            if (pluginId != null) {
                getPluginDescriptor(pluginId).setSoftClassLoader(entry.getValue());
            }
        }

        logger.log(Level.FINE, "Parsing Core Extensions ...");

        HashSet<String> coreInterfaces = new HashSet<String>();
        SoftClassLoader coreSoftClassLoader = softClassLoaders.get(null);
        try {
            PluginManagerCache.PluginCache pcache = pluginManagerCache.getPluginCache(null);
            for (URL coreURL : getCoreURLs()) {
                SoftReflector.visitURLClasses(coreURL, new InterfacesAutoLoadVisitor(coreInterfaces, extensionFilter, application), coreSoftClassLoader);
                pluginManagerCache.getExtensions().addAll(coreInterfaces);
            }
            pcache.setExtensions(coreInterfaces);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Parsing Core Interfaces Error", e);
        }

        logger.log(Level.CONFIG, "Parsing Plugins Extensions ...");

        for (PluginDescriptor descriptor : descriptors) {
//            TLogList logList = new TLogList(getLogger(), descriptor.getLog());
            if (descriptor.isValid()) {
                try {
                    PluginManagerCache.PluginCache pluginCache = pluginManagerCache.getPluginCache(descriptor.getId());
                    HashSet<String> interfaces = new HashSet<String>();
                    SoftReflector.visitURLClasses(descriptor.getPluginURL(), new InterfacesAutoLoadVisitor(interfaces, extensionFilter, application), softClassLoaders.get(descriptor.getId()));
                    pluginManagerCache.getExtensions().addAll(interfaces);
                    pluginCache.setExtensions(interfaces);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Parsing Plugins Interfaces Error (" + descriptor + ")", e);
                }
            } else {
                logger.log(Level.SEVERE, "Invalid {0}. Parsing Plugins Interfaces ignored!", descriptor);
            }
        }

        logger.log(Level.CONFIG, "Parsing Core Implementations ...");
        try {
            PluginManagerCache.PluginCache pluginCache = pluginManagerCache.getPluginCache(null);
            for (URL coreURL : getCoreURLs()) {
                logger.log(Level.CONFIG, "\tParsing Core Implementations : Looking into {0}", coreURL);
                SoftReflector.visitURLClasses(coreURL, new ImplementationsAutoLoadVisitor(pluginCache, pluginManagerCache, ignoreImplementationFilter, lifeCycleMethodsFilter, injectionFieldsFilter, application), coreSoftClassLoader);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Parsing Core Implementations Error", e);
        }

        logger.log(Level.CONFIG, "Parsing Plugins Implementations ...");
        for (PluginDescriptor pluginDescriptor : descriptors) {
//            TLogList logList = new TLogList(getLogger(), pluginDescriptor.getLog());
            if (pluginDescriptor.isValid()) {
                try {
                    boolean enableLoading = pluginDescriptor.isDynamicLoading();
                    String pluginClassName = null;
                    PluginManagerCache.PluginCache cache = pluginManagerCache.getPluginCache(pluginDescriptor.getId());
                    SoftReflector.visitURLClasses(pluginDescriptor.getPluginURL(), new ImplementationsAutoLoadVisitor(cache, pluginManagerCache, ignoreImplementationFilter, lifeCycleMethodsFilter, injectionFieldsFilter, application), softClassLoaders.get(pluginDescriptor.getId()));
                    pluginClassName = pluginDescriptor.getPluginClassName();
                    SoftClass pluginClass = null;
                    if (pluginClassName == null) {
                        Set<PluginManagerCache.ImplementationCache> classes = cache.getImplementations(getPluginClass());
                        if (classes != null && classes.size() > 0) {
                            pluginClassName = classes.toArray(new PluginManagerCache.ImplementationCache[classes.size()])[0].getImplementationName();
                        }
                    }
                    if (pluginClassName != null) {
                        pluginClass = pluginDescriptor.getSoftClassLoader().findClass(pluginClassName);
                    }

                    cache.setPluginClassName(pluginClassName);
                    cache.setEnableLoading(enableLoading);

                    String messageSet = null;
                    String iconSet = null;
                    if (pluginClass != null) {
                        for (SoftAnnotation ann : pluginClass.getAnnotations()) {
                            if (ann.getName().equals(PluginInfo.class.getName())) {
                                for (SoftAnnotationAttribute attr : ann.getAtributes()) {
                                    if (attr.getName().equals("messageSet")) {
                                        messageSet = (String) attr.getValue();
                                    } else if (attr.getName().equals("iconSet")) {
                                        iconSet = (String) attr.getValue();
                                    }
                                }
                            }
                        }
                    }
                    cache.setMessageSet(messageSet);
                    cache.setIconSet(iconSet);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Failed to parse plugin implementation for " + pluginDescriptor, e);
                }
                //splashScreen.setProgress(progressInit + progressStep * progressIndex / pluginsInfosCollection.size());
                //progressIndex++;
            } else {
                logger.log(Level.WARNING, "\tParsing Plugins Implementations : ignore non valide plugin  {0}", pluginDescriptor.getId());
            }
        }
        pluginManagerCache.build(priorityComparator);
        return pluginManagerCache;
    }

    protected PluginManagerCache getCache() {
        return cache;
    }

    public void build(ProgressMonitor monitor) {
        ProgressMonitor[] mon = ProgressMonitorUtils.split(monitor, 10);
        int i = 0;
        buildStart(mon[i++]);
        
        buildPrepare(mon[i++]);
        buildPostPrepare(mon[i++]);
        
        buildLookupPlugins(mon[i++]);
        buildPostLookupPlugins(mon[i++]);
        
        buildLookupExtensions(mon[i++]);
        buildPostLookupExtensions(mon[i++]);
        
        buildCreatePlugins(mon[i++]);
        buildPostCreatePlugins(mon[i++]);
        
        buildEnd(mon[i++]);
    }

    protected void buildLookupExtensions(ProgressMonitor monitor) {
        Map<String, ExtensionDescriptor> confs = new HashMap<String, ExtensionDescriptor>();

        logger.log(Level.CONFIG, "Registering {0} extensions ...", getCache().getPlugins().size());
        for (Map.Entry<String, PluginManagerCache.PluginCache> entry : getCache().getPlugins().entrySet()) {
            String id = entry.getKey();
            PluginManagerCache.PluginCache cache = entry.getValue();
            PluginDescriptor pluginInfo = id == null ? null : getPluginDescriptor(id);
            if (id == null || (pluginInfo.isValid() && isPluginEnabled(id))) {
                Set<String> pluginInterfaces = cache.getExtensions();
                for (String pluginInterface : pluginInterfaces) {
                    Class ii = null;
                    try {
                        ii = Class.forName(pluginInterface, true, pluginInfo == null ? getCoreClassLoader() : pluginInfo.getClassLoader());
                    } catch (ClassNotFoundException e) {
                        //should not happen
                        throw new IllegalArgumentException("Should never happen");
                    }
                    Extension annInterface = (Extension) ii.getAnnotation(Extension.class);
                    ExtensionDescriptor configuration = confs.get(pluginInterface);
                    if (configuration == null) {
                        String group = annInterface.group();
                        Class[] alternatives = annInterface.shares();
                        configuration = new ExtensionDescriptor(ii, pluginInfo, group, null);
                        configuration.setCustomizable(annInterface.customizable());
                        for (Class alternative : alternatives) {
                            configuration.addAlternative(alternative, pluginInfo);
                        }
                        confs.put(ii.getName(), configuration);
                    }
                }
            }
        }

        logger.log(Level.CONFIG, "Registering {0} Implementations ...", getCache().getImplementations().size());
        for (PluginManagerCache.ImplementationCache impl : getCache().getImplementations()) {
            PluginManagerCache.PluginCache pcache = impl.getPluginCache();
            String id = pcache.getId();
            String prettyId = id == null ? "{CORE}" : id;
            PluginDescriptor pluginInfo = id == null ? null : getPluginDescriptor(id);
            if (id == null || (pluginInfo.isValid() && isPluginEnabled(id))) {
                ExtensionDescriptor configuration = confs.get(impl.getExtensionName());
                if (configuration == null) {
                    logger.log(Level.SEVERE, "Interface not found {0}", impl.getExtensionName());
                } else {
                    ClassLoader loader = pluginInfo == null ? getCoreClassLoader() : pluginInfo.getClassLoader();
                    try {
                        configuration.add(new ImplementationDescriptor(Class.forName(impl.getImplementationName(), true, loader), pluginInfo));
                    } catch (ClassNotFoundException e) {
                        if (pluginInfo != null) {
                            pluginInfo.getLog().error("[" + prettyId + "] : Unable load implementation " + impl, e);
                        }
                        logger.log(Level.SEVERE, "[" + prettyId + "] : Unable load implementation " + impl, e);
                    } catch (NoClassDefFoundError e) {
                        if (pluginInfo != null) {
                            pluginInfo.getLog().error("[" + prettyId + "] Unable load implementation " + impl, e);
                        }
                        logger.log(Level.SEVERE, "[" + prettyId + "] Unable load implementation " + impl, e);
                    }
                }
            } else {
                logger.log(Level.SEVERE, "[{0}] Unable load implementation {1} because plugin {2} is {3} and {4}", new Object[]{prettyId, impl, prettyId, pluginInfo.isValid() ?"valid":"invalid", pluginInfo.isValid() ?"valid":"invalid"});
            }
        }
        for (PluginManagerCache.ImplementationCache impl : getCache().getImplementationFactories()) {
            PluginManagerCache.PluginCache pcache = impl.getPluginCache();
            String id = pcache.getId();
            PluginDescriptor pluginInfo = id == null ? null : getPluginDescriptor(id);
            if (id == null || (pluginInfo.isValid() && isPluginEnabled(id))) {
                ExtensionDescriptor configuration = confs.get(impl.getExtensionName());
                if (configuration == null) {
                    logger.log(Level.SEVERE, "Interface not found {0}", impl.getExtensionName());
                } else {
                    try {
                        configuration.add(new ImplementationFactoryDescriptor((Class<? extends ExtensionFactory>) Class.forName(impl.getImplementationName(), true, pluginInfo == null ? getCoreClassLoader() : pluginInfo.getClassLoader()), pluginInfo));
                    } catch (ClassNotFoundException e) {
                        if (pluginInfo != null) {
                            pluginInfo.getLog().error("Unable load class " + impl, e);
                        }
                        logger.log(Level.SEVERE, "[" + pluginInfo + "] Unable load class " + impl, e);
                    }
                }
            }
        }
        extensions = confs.values().toArray(new ExtensionDescriptor[confs.size()]);
    }

    public void initializeInstance(Object instance, FieldValueProviderManager fieldValueProviderManager, String pluginId) {
        Class<? extends Object> clazz = instance.getClass();
        PluginManagerCache.ClassCache classCache = cache.getClassCache(clazz.getName(), false);
        if (classCache == null) {
            logger.log(Level.FINE, "Force Loading class for initialization : {0}", clazz.getName());
            classCache = cache.getClassCache(clazz.getName(), true);
            Reflector.ClassInfo cinfo = Reflector.getClassInfo(clazz, new FieldFilter() {

                public boolean accept(Field field) {
                    return injectionFieldsFilter.accept(new SoftFieldImpl(field));
                }
            }, new MethodFilter() {

                public boolean accept(Method method) {
                    return lifeCycleMethodsFilter.accept(new SoftMethodImpl(method));
                }
            }, Reflector.FIELDS | Reflector.METHODS);
            for (Field field : cinfo.getFields()) {
                for (Annotation annotation : field.getAnnotations()) {
                    classCache.addField(field.getDeclaringClass().getName(), field.getName(), annotation.annotationType().getName());
                    //log.debug("[" + clazz.getName() + "] Detected Field : " + field.toString());
                }
            }
            for (Method method : cinfo.getMethods()) {
                for (Annotation annotation : method.getAnnotations()) {
                    classCache.addMethod(method.getDeclaringClass().getName(), method.getName(), new String[0], annotation.annotationType().getName());
                    //log.debug("[" + clazz.getName() + "] Detected Method : " + method.toString());
                }
            }
        }
        if (classCache != null) {
            PluginDescriptor descriptor = getPluginDescriptor(pluginId);
            if (descriptor == null) {
                logger.log(Level.SEVERE, "Unknown plugin {0}", pluginId);
            }
            ClassLoader loader = descriptor.getClassLoader();
            for (PluginManagerCache.FieldCache fieldCache : classCache.getFields(Inject.class.getName())) {
                try {
                    Field field = fieldCache.toField(loader);
                    Class type = field.getAnnotation(Inject.class).type();
                    if (type.equals(Object.class)) {
                        type = field.getType();
                    }
                    Object value = null;
                    boolean ok = false;
                    try {
                        value = fieldValueProviderManager.getValue(type, descriptor);
                        ok = true;
                    } catch (NoSuchElementException e) {
                        logger.log(Level.SEVERE, "No reference provider for {0}", field);
                        ok = false;
                    }
                    if (ok) {
                        field.setAccessible(true);
                        try {
                            field.set(instance, value);
                        } catch (Exception ee) {
                            logger.log(Level.SEVERE, "Field injection failed for " + field, ee);
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Field injection failed ", e);
                }
            }
            for (PluginManagerCache.FieldCache fieldCache : classCache.getFields(NewInstance.class.getName())) {
                try {
                    Field field = fieldCache.toField(loader);
                    Object value = null;
                    boolean ok = false;
                    try {
                        value = fieldValueProviderManager.createValue(field, descriptor);
                        ok = true;
                    } catch (NoSuchElementException e) {
                        logger.log(Level.SEVERE, "No new instance provider for {0}", field);
                        ok = false;
                    }
                    if (ok) {
                        field.setAccessible(true);
                        field.set(instance, value);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "NewInstance failed", e);
                }
            }
            for (PluginManagerCache.MethodCache methodCache : classCache.getMethods(Initializer.class.getName())) {
                try {
                    Method method = methodCache.toMethod(loader);
                    method.setAccessible(true);
                    method.invoke(instance);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Initializer failed", e);
                }
            }
        }
    }

    public List<URL> getCoreURLs() {
        return coreURLs;
    }

    protected void setCoreURLs(List<URL> coreURLs) {
        this.coreURLs = coreURLs;
    }

    public ClassLoader getCoreClassLoader() {
        return coreClassLoader;
    }

    protected void setCoreClassLoader(ClassLoader coreClassLoader) {
        this.coreClassLoader = coreClassLoader;
    }

    public ExtensionDescriptor[] getExtensions() {
        return extensions;
    }

    public void setLocalRepository(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    @Override
    public LocalRepository getLocalRepository() {
        return localRepository;
    }

    public String[] getAllDependencies(String pluginId) {
        Set<String> deps = new HashSet<String>();
        Stack<String> stack = new Stack<String>();
        stack.add(pluginId);
        while (!stack.isEmpty()) {
            String p = stack.pop();
            PluginDescriptor dd = getPluginDescriptor(p);
            if (dd != null) {
                PluginDependency[] dependencies = dd.getDependencies();
                if (dependencies != null) {
                    for (PluginDependency pd : dependencies) {
                        if (!deps.contains(pd.getId())) {
                            deps.add(pd.getId());
                            stack.add(pd.getId());
                        }
                    }
                }
            }
        }
        return deps.toArray(new String[deps.size()]);
    }
}
