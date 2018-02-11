/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.prs.softreflect;

import net.vpc.common.prs.softreflect.classloader.SoftClassLoader;

/**
 *
 * @author vpc
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
