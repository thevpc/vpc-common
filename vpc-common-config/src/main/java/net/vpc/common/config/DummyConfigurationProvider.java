/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.config;

/**
 *
 * @author vpc
 */
public class DummyConfigurationProvider implements ConfigurationProvider{
    private Configuration configuration=new FileConfiguration();

    public Configuration getConfiguration() {
        return configuration;
    }

    public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
        //
    }

    public void removeConfigurationChangeListener(ConfigurationChangeListener listener) {
        //
    }
    
}
