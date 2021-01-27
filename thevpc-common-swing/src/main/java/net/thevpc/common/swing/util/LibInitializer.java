/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.swing.util;

/**
 *
 * @author thevpc
 */
public interface LibInitializer {
    public void initializeLibrary();
    public String[] getDependencies();
}
