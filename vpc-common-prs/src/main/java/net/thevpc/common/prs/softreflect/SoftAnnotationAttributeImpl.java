/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

/**
 *
 * @author thevpc
 */
public class SoftAnnotationAttributeImpl implements SoftAnnotationAttribute{
    private String name;
    private Object value;

    public SoftAnnotationAttributeImpl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
}
