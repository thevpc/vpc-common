/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.prs.softreflect;

/**
 *
 * @author vpc
 */
public interface SoftField {
    public String getName();
    public String getDeclaringClassName();
    public int getModifiers();
    public SoftAnnotation[] getAnnotations();
}
