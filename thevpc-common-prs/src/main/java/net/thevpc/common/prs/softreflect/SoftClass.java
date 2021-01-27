/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;

/**
 *
 * @author thevpc
 */
public interface SoftClass {
    public String getName();
    public int getModifiers();
    public boolean isInterface();
    public SoftField[] getDeclaredFields();
    public SoftMethod[] getDeclaredMethods();
    public String getSuperclass();
    public String[] getInterfaces();
    public boolean hasBeanConstructor();
    public SoftClassLoader getClassLoader();
    public void setClassLoader(SoftClassLoader loader);
    public SoftAnnotation[] getAnnotations();
}
