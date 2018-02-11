/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.softreflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class SoftAnnotationImpl implements SoftAnnotation {

    private String name;
    private SoftAnnotationAttribute[] atributes;

    public SoftAnnotationImpl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoftAnnotationAttribute[] getAtributes() {
        return atributes;
    }

    public void setAtributes(SoftAnnotationAttribute[] atributes) {
        this.atributes = atributes;
    }

    public SoftAnnotationImpl(Annotation a) {
        setName(a.annotationType().getName());
        List<SoftAnnotationAttribute> annotationsAttributes = new ArrayList<SoftAnnotationAttribute>();
        for (Method m : a.annotationType().getDeclaredMethods()) {
            SoftAnnotationAttributeImpl at = new SoftAnnotationAttributeImpl();
            at.setName(m.getName());
            try {
                at.setValue(m.invoke(a));
            } catch (Exception ex) {
                //ignore
            }
            annotationsAttributes.add(at);
        }
        setAtributes(annotationsAttributes.toArray(new SoftAnnotationAttribute[annotationsAttributes.size()]));
    }
}
