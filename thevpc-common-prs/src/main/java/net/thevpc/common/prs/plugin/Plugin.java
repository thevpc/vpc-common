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

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.messageset.MessageSet;
import net.thevpc.common.prs.ResourceSetHolder;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Locale;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 27 août 2007 19:23:14
 */
public interface Plugin<T, Pm> extends ResourceSetHolder,LoggerProvider {

    public void pluginInstalled();

    public void pluginUninstalled();

    public void pluginRegistered();

    public void pluginUnregistered();

    /**
     * called to initialize Application component.
     * Mainly we could configure Factory here with
     * <pre>
     * registerImplementation(AnyInterface.class, AnyClass.class,true)
     * </pre>
     * it is recommended not to use directly
     * <pre>
     *   getApplication().getFactory().getConfiguration(AnyInterface.class).add(AnyClass.class);
     * </pre>
     * <p>
     * system plugin should call the following
     * <pre>
     * getApplication().setDefaultFactoryConfigurations(createFactoryConfigurations(getMessageSet()));
     * </pre>
     */
    public void applicationInitializing();

    public void applicationOpening();
    
    public void applicationConfiguring();

    public void applicationReady();

    public void applicationClosing();

    public T getApplication();
    
    public Pm getPluginManager();
    
    public void init(T app, Pm pluginManager);

    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public void setInitialized(boolean enabled);

    public boolean isInitialized();

    public void setLocale(Locale lcoale);

    public PluginDescriptor getDescriptor();

    public void setDescriptor(PluginDescriptor info);

    public MessageSet getMessageSet();

    public IconSet getIconSet();

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void setMessageSet(String bundleName);

    void setIconSet(String iconSetBundleName);

    /**
     * Plugin Unique Identifier
     * must return getPluginDescriptor().getId()
     *
     * @return getPluginDescriptor().getId()
     */
    String getId();
    
    public File getVarFolder();
}
