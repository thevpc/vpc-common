/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

/**
 *
 * @author thevpc
 */
public interface SoftField {
    public String getName();
    public String getDeclaringClassName();
    public int getModifiers();
    public SoftAnnotation[] getAnnotations();
}
