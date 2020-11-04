/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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
 * @creationtime 27 août 2007 19:23:14
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
     * <p/>
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
