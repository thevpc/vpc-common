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
package net.vpc.common.prs.plugin;

import net.vpc.common.prs.iconset.IconSet;
import net.vpc.common.prs.iconset.MultiIconSet;
import net.vpc.common.prs.messageset.MessageSet;
import net.vpc.common.prs.messageset.MessageSetResourceBundleWrapper;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.vpc.common.prs.log.SimpleLogFormatter;
import net.vpc.common.prs.iconset.DefaultIconSet;
import net.vpc.common.prs.iconset.IconSetManager;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 août 2007 19:30:54
 */
public class DefaultPlugin<T extends PluggableApplication, Pm extends PluginManager> implements Plugin<T, Pm> {

    private T application;
    private Pm pluginManager;
    private boolean enabled = true;
    private boolean initialized;
    private Locale locale;
    private MessageSet messageSet;
    private IconSet iconSet;
    private PluginDescriptor descriptor;
    private PropertyChangeSupport pcs;
    private String fallBackIconSetName;

    public DefaultPlugin() {
        pcs = new PropertyChangeSupport(this);
    }

    public String getId() {
        return getDescriptor().getId();
    }

    /**
     * Implementation Owner is the owner instance that will be used as an owner of all objects created by this plugin
     *
     * @return Implementation Owner
     */
    protected Object getImplementationOwner() {
        return this;
    }

//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
    public void init(T client, Pm pluginManager) {
        this.application = client;
        this.pluginManager = pluginManager;
        Logger sessionLogger = getLogger("");
        File logFile = new File(getPluginManager().getPluginVarFolder(getId()), "log/plugin-%u.log");
        logFile.getParentFile().mkdirs();
        boolean loggerAlreadyInitialized = false;
        for (Handler handler : sessionLogger.getHandlers()) {
            if (handler instanceof PluginFileHandler) {
                PluginFileHandler fh = (PluginFileHandler) handler;
                if (fh.getPluginId().equals(getId())) {
                    loggerAlreadyInitialized = true;
                    break;
                }
            }
        }
        if (!loggerAlreadyInitialized) {
            try {
                sessionLogger.addHandler(new PluginFileHandler(getId(), logFile.getPath(), 1024 * 1024 * 5, 3, true));
            } catch (Exception ex) {
                application.getLogger(DefaultPlugin.class.getName()).log(Level.SEVERE, "Unable to configure plugin logger", ex);
            }
        }
    }

    @Override
    public Pm getPluginManager() {
        return pluginManager; 
    }
    
    @Override
    public File getVarFolder(){
        return getPluginManager().getPluginVarFolder(getId());
    }
    

    @Override
    public void applicationInitializing() {
        //do nohing
    }

    @Override
    public void applicationOpening() {
        //do nohing
    }

    @Override
    public void applicationReady() {
    }

    @Override
    public void applicationClosing() {
    }

    @Override
    public void applicationConfiguring() {
        
    }

    
    public T getApplication() {
        return application;
    }

    

    public Locale getLocale() {
        return locale == null ? Locale.getDefault() : locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        if (messageSet != null) {
            messageSet.setLocale(getLocale());
        }
        if (iconSet != null) {
            iconSet.setLocale(getLocale());
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        boolean old = this.enabled;
        this.enabled = enabled;
        pcs.firePropertyChange("enabled", old, enabled);
    }

    public MessageSet getMessageSet() {
        return messageSet == null ? getApplicationMessageSet() : messageSet;
    }

    public MessageSet getApplicationMessageSet() {
        return null;
    }

    public IconSet getApplicationIconSet() {
        return null;
    }

    public IconSet getIconSet() {
        return iconSet == null ? getApplicationIconSet() : iconSet;
    }

    public void setIconSet(IconSet iconSet) {
        this.iconSet = new MultiIconSet(
                getApplication(),
                iconSet,
                getApplicationIconSet(),
                //default iconSet
                getFallBackIconSetName() == null ? null : IconSetManager.getIconSet(getFallBackIconSetName()));
    }

    @Override
    public String toString() {
        return getId() + "@" + getClass().getName();
    }

    public void setMessageSet(MessageSet messageSet) {
        this.messageSet = new MessageSet(getApplication(), getApplicationMessageSet(), messageSet);
    }

    public void setMessageSet(String bundleName) {
        MessageSet messageSetExtension = new MessageSet(getApplication(),new MessageSetResourceBundleWrapper(bundleName, getLocale(), getClass().getClassLoader()));
        this.messageSet = new MessageSet(getApplication(),getApplicationMessageSet(), messageSetExtension);
    }

    public PluginDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(PluginDescriptor info) {
        this.descriptor = info;
    }

    public void setIconSet(String iconSetBundleName) {
        setIconSet(new MultiIconSet(
                getApplication(),
                new DefaultIconSet(iconSetBundleName, getClass().getClassLoader(), getLocale(), application),
                getApplicationIconSet(),
                //default iconSet
                getFallBackIconSetName() == null ? null : IconSetManager.getIconSet(getFallBackIconSetName())));
    }

    public void pluginRegistered() {
        //
    }

    public void pluginUnregistered() {
        //
    }

    public void pluginInstalled() {
        //
    }

    public void pluginUninstalled() {
        //
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public String getFallBackIconSetName() {
        return fallBackIconSetName;
    }

    public void setFallBackIconSetName(String fallBackIconSetName) {
        this.fallBackIconSetName = fallBackIconSetName;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

//    @Override
    public Logger getLogger(String category) {
        if (category == null || category.isEmpty()) {
            return getApplication().getLogger("plugins." + getId());
        }
        return getApplication().getLogger("plugins." + getId() + "." + category);
    }

    public <T> T instantiate(Class<T> clazz) {
        return getApplication().getFactory().instantiate(clazz,getDescriptor());
    }

    private static class PluginFileHandler extends FileHandler {

        private String pluginId;

        public PluginFileHandler(String pluginId, String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
            super(pattern, limit, count, append);
            this.pluginId = pluginId;
            setFormatter(new SimpleLogFormatter());
        }

        public String getPluginId() {
            return pluginId;
        }
    }
}
