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

import net.thevpc.common.prs.factory.ExtensionDescriptor;
import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;
import net.thevpc.common.prs.Version;

import java.io.File;
import java.net.URL;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 août 2007 19:39:24
 */
public interface PluginManager<App extends PluggableApplication,Plug extends Plugin> {

    public Plug[] getAllPlugins();

    /**
     * only valid plugins
     *
     * @return all plugins
     */
    public Plug[] getEnabledPlugins();
    
    public Plug[] getPlugins(PluginFilter<Plug> filter);

    public void init(App application, Version applicationVersion);


    /**
     * @param infos
     * @return false if plugin is not yet registred but registration failes (class not found, etc)
     *         throws InvalidPluginException when desc is null
     *         throws PluginAlreadyRegistredException if already registred
     * @throws InvalidPluginException when plugin is invalid
     */
    public boolean registerPlugins(PluginDescriptor... infos) throws InvalidPluginException;

    public void unregisterPlugin(String pluginId) throws PluginException;

    public Plug getPlugin(String pluginId);

    public Plug getValidPlugin(String pluginId);

    public PluginDescriptor.Status getPluginStatus(String pluginId);

    public boolean isPluginEnabled(String pluginId);

    void unregisterAvailablePlugins() throws PluginException;

    App getApplication();

    void close();

    /**
     *
     * @param plugin pluginId to look for in plugin repositories
     * @param installDependencies if true, all dependent plugins will be installed too
     * @throws PluginException if problem
     */
    void installPlugin(String plugin, VersionInterval ve, boolean installDependencies) throws PluginException;

    //void installPlugin(String plugin,boolean installDependencies) throws PluginException;

    void installPlugin(URL newPluginUrl,boolean installDependencies) throws PluginException;
    

    void uninstallPlugin(String pluginId) throws PluginException;

    void addPluginRepository(PluginRepository r);

    void removePluginRepository(PluginRepository r);

    PluginRepository[] getPluginRepositories();

//    PluginInfo[] getInstallablePluginInfos();

//    UrlCacheManager getUrlCacheManager();

    PluginDescriptor[] getAvailablePluginDescriptors();

    /**
     * All System/Core Plugins
     * @return Array of all registred System Plugins
     */
    Plug[] getSystemPlugins();
    
    /**
     * returns the SystemPlugin
     * similar to getSystemPlugins()[0].
     * should return null il no plugin specified. TODO? should add Specific Exception?
     * 
     * @return the SystemPlugin
     */
    Version getApplicationVersion();

    public PluginDescriptor getPluginDescriptor(String id);

//    Set<Class> getInterfaces(Plug plugin);

    public File getPluginVarFolder(String pluginId) throws PluginException;

    void initializeInstance(Object instance, FieldValueProviderManager fieldValueProviderManager, String pluginId);


    public File getPluginsVarFolder() throws PluginException ;

    public BlendedFile getPluginsFolder() throws PluginException ;

    ExtensionDescriptor[] getExtensions();

    public LocalRepository getLocalRepository();

    SoftClassLoader getSoftCoreClassLoader();
    ClassLoader getCoreClassLoader();
}
