/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.prs.softreflect.classloader;

import net.vpc.common.prs.softreflect.SoftClass;

/**
 *
 * @author vpc
 */
public interface SoftClassLoader {
    SoftClass findClass(String name);
}
