/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thevpc
 */
public class ConfigurationManagerImpl implements ConfigurationManager {

    private Configuration startupConfiguration;
    private Configuration sharedConfiguration;
    private Configuration userConfiguration;
    private List<ConfigurationChangeListener> userConfigurationChangeListeners;
    private List<ConfigurationChangeListener> startupConfigurationChangeListeners;
    private List<ConfigurationChangeListener> sharedConfigurationChangeListeners;
    private List<ConfigurationChangeListener> allConfigurationChangeListeners;
    private ConfigurationProvider startupConfigurationProvider = new ConfigurationProvider() {

        public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
            addStartupConfigurationChangeListener(listener);
        }

        public void removeConfigurationChangeListener(ConfigurationChangeListener listener) {
            removeStartupConfigurationChangeListener(listener);
        }

        public Configuration getConfiguration() {
            return getStartupConfiguration();
        }
    };
    private ConfigurationProvider sharedConfigurationProvider = new ConfigurationProvider() {

        public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
            addSharedConfigurationChangeListener(listener);
        }

        public void removeConfigurationChangeListener(ConfigurationChangeListener listener) {
            removeSharedConfigurationChangeListener(listener);
        }

        public Configuration getConfiguration() {
            return getSharedConfiguration();
        }
    };
    private ConfigurationProvider userConfigurationProvider = new ConfigurationProvider() {

        public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
            addUserConfigurationChangeListener(listener);
        }

        public void removeConfigurationChangeListener(ConfigurationChangeListener listener) {
            removeUserConfigurationChangeListener(listener);
        }

        public Configuration getConfiguration() {
            return getUserConfiguration();
        }
    };

    public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (allConfigurationChangeListeners == null) {
            allConfigurationChangeListeners = new ArrayList<ConfigurationChangeListener>();
        }
        allConfigurationChangeListeners.add(listener);
    }

    public void addSharedConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (sharedConfigurationChangeListeners == null) {
            sharedConfigurationChangeListeners = new ArrayList<ConfigurationChangeListener>();
        }
        sharedConfigurationChangeListeners.add(listener);
    }

    public void addStartupConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (startupConfigurationChangeListeners == null) {
            startupConfigurationChangeListeners = new ArrayList<ConfigurationChangeListener>();
        }
        startupConfigurationChangeListeners.add(listener);
    }

    public void addUserConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (userConfigurationChangeListeners == null) {
            userConfigurationChangeListeners = new ArrayList<ConfigurationChangeListener>();
        }
        userConfigurationChangeListeners.add(listener);
    }

    public void removeConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (allConfigurationChangeListeners != null) {
            allConfigurationChangeListeners.remove(listener);
        }
    }

    public void removeSharedConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (sharedConfigurationChangeListeners != null) {
            sharedConfigurationChangeListeners.remove(listener);
        }
    }

    public void removeStartupConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (startupConfigurationChangeListeners != null) {
            startupConfigurationChangeListeners.remove(listener);
        }
    }

    public void removeUserConfigurationChangeListener(ConfigurationChangeListener listener) {
        if (userConfigurationChangeListeners != null) {
            userConfigurationChangeListeners.remove(listener);
        }
    }

    public Configuration getSharedConfiguration() {
        return sharedConfiguration;
    }

    public void setSharedConfiguration(Configuration sharedConfiguration) {
        Configuration oldv = this.sharedConfiguration;
        this.sharedConfiguration = sharedConfiguration;
        fireEvent("Shared", oldv, sharedConfiguration, sharedConfigurationChangeListeners);
    }

    public Configuration getStartupConfiguration() {
        return startupConfiguration;
    }

    public void setStartupConfiguration(Configuration startupConfiguration) {
        Configuration oldv = this.startupConfiguration;
        this.startupConfiguration = startupConfiguration;
        fireEvent("Startup", oldv, startupConfiguration, startupConfigurationChangeListeners);
    }

    public Configuration getUserConfiguration() {
        return userConfiguration;
    }

    public void setUserConfiguration(Configuration userConfiguration) {
        Configuration oldv = this.userConfiguration;
        this.userConfiguration = userConfiguration;
        fireEvent("User", oldv, userConfiguration, userConfigurationChangeListeners);
    }

    public ConfigurationProvider getSharedConfigurationProvider() {
        return sharedConfigurationProvider;
    }

    public ConfigurationProvider getStartupConfigurationProvider() {
        return startupConfigurationProvider;
    }

    public ConfigurationProvider getUserConfigurationProvider() {
        return userConfigurationProvider;
    }

    private void fireEvent(String id, Configuration oldv, Configuration newv, List<ConfigurationChangeListener> listeners) {
        if (oldv != newv) {
            ConfigurationChangeEvent e = null;
            if (listeners != null) {
                for (ConfigurationChangeListener ll : listeners) {
                    if (e == null) {
                        e = new ConfigurationChangeEvent(id, oldv, newv);
                    }
                    ll.configurationChanged(e);
                }
            }
            if (allConfigurationChangeListeners != null) {
                for (ConfigurationChangeListener ll : allConfigurationChangeListeners) {
                    if (e == null) {
                        e = new ConfigurationChangeEvent(id, oldv, newv);
                    }
                    ll.configurationChanged(e);
                }
            }
        }
    }

    public void store()
            throws IOException {
        if (getStartupConfiguration() != null) {
            getStartupConfiguration().store();
        }
        if (getSharedConfiguration() != null) {
            getSharedConfiguration().store();
        }
        if (getUserConfiguration() != null) {
            getUserConfiguration().store();
        }
    }
}
