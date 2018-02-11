/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.plugin;

/**
 *
 * @author vpc
 */
public interface PluginFilter<Plug extends Plugin> {
    boolean accept(Plug plugin);
}
