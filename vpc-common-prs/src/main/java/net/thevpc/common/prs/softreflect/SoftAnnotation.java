/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.softreflect;

/**
 *
 * @author vpc
 */
public interface SoftAnnotation {

    public String getName();

    public SoftAnnotationAttribute[] getAtributes();

    public SoftAnnotationAttribute getAttribute(String name);
}
