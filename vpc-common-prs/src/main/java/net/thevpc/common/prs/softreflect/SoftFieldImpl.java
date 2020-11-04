/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.softreflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author vpc
 */
public class SoftFieldImpl implements SoftField {

    private String name;
    private String declaringClassName;
    private int modifiers;
    private SoftAnnotation[] annotations;

    public SoftAnnotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(SoftAnnotation[] annotations) {
        this.annotations = annotations;
    }

    public SoftFieldImpl() {
    }

    public SoftFieldImpl(Field field) {
        setName(field.getName());
        setModifiers(field.getModifiers());
        setDeclaringClassName(field.getDeclaringClass().getName());
        Annotation[] annotations = field.getAnnotations();
        SoftAnnotation[] rAnnotations=new SoftAnnotation[annotations.length];
        for (int i = 0; i < rAnnotations.length; i++) {
            rAnnotations[i]=new SoftAnnotationImpl(annotations[i]);
        }
        setAnnotations(rAnnotations);
    }

    public String getDeclaringClassName() {
        return declaringClassName;
    }

    public void setDeclaringClassName(String declaringClassName) {
        this.declaringClassName = declaringClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }
}
