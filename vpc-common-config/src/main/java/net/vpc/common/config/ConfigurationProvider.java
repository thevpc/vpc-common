/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.config;

/**
 *
 * @author vpc
 */
public interface ConfigurationProvider {

    public Configuration getConfiguration();

    public void addConfigurationChangeListener(ConfigurationChangeListener listener);

    public void removeConfigurationChangeListener(ConfigurationChangeListener listener);
}
