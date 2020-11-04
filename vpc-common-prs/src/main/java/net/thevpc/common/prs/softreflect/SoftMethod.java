/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

/**
 *
 * @author vpc
 */
public interface SoftMethod {
    String getName();
    int getModifiers();
    String[] getParameterTypes();
    SoftClass getDeclaringClass();
    String getDeclaringClassName();
    public SoftAnnotation[] getAnnotations();
}
