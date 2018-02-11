/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.swings.util;

/**
 *
 * @author vpc
 */
public interface LibInitializer {
    public void initializeLibrary();
    public String[] getDependencies();
}
