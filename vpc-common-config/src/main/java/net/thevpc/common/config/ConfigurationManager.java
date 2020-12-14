/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.config;

import java.io.IOException;

/**
 *
 * @author thevpc
 */
public interface ConfigurationManager {

    public void store()
            throws IOException ;

    public Configuration getStartupConfiguration();

    public Configuration getSharedConfiguration();

    public Configuration getUserConfiguration();

    public void setStartupConfiguration(Configuration value);

    public void setSharedConfiguration(Configuration value);

    public void setUserConfiguration(Configuration value);

    public ConfigurationProvider getStartupConfigurationProvider();

    public ConfigurationProvider getSharedConfigurationProvider();

    public ConfigurationProvider getUserConfigurationProvider();

    public void addConfigurationChangeListener(ConfigurationChangeListener listener);

    public void addSharedConfigurationChangeListener(ConfigurationChangeListener listener);

    public void addStartupConfigurationChangeListener(ConfigurationChangeListener listener);

    public void addUserConfigurationChangeListener(ConfigurationChangeListener listener);

    public void removeConfigurationChangeListener(ConfigurationChangeListener listener);

    public void removeSharedConfigurationChangeListener(ConfigurationChangeListener listener);

    public void removeStartupConfigurationChangeListener(ConfigurationChangeListener listener);

    public void removeUserConfigurationChangeListener(ConfigurationChangeListener listener);
}
