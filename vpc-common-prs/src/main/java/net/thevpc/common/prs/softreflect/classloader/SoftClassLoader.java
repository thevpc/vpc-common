/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect.classloader;

import net.thevpc.common.prs.softreflect.SoftClass;

/**
 *
 * @author thevpc
 */
public interface SoftClassLoader {
    SoftClass findClass(String name);
}
