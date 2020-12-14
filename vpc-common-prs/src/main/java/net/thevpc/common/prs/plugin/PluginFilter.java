/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.plugin;

/**
 *
 * @author thevpc
 */
public interface PluginFilter<Plug extends Plugin> {
    boolean accept(Plug plugin);
}
