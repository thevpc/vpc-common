/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;

/**
 *
 * @author vpc
 */
public class SoftClassImpl implements SoftClass{
    private String name;
    private int modifiers;
    private boolean _isInterface;
    private SoftField[] declaredFields;
    private SoftMethod[] declaredMethods;
    private String superclass;
    private String[] interfaces;
    private boolean beanConstructor;
    private SoftClassLoader classLoader;
    private SoftAnnotation[] annotations;

    public SoftClassImpl() {
    }

    
    public SoftAnnotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(SoftAnnotation[] annotations) {
        this.annotations = annotations;
    }
    
    public boolean isBeanConstructor() {
        return beanConstructor;
    }

    public void setBeanConstructor(boolean beanConstructor) {
        this.beanConstructor = beanConstructor;
    }

    public SoftClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(SoftClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public SoftField[] getDeclaredFields() {
        return declaredFields;
    }

    public void setDeclaredFields(SoftField[] declaredFields) {
        this.declaredFields = declaredFields;
    }

    public SoftMethod[] getDeclaredMethods() {
        return declaredMethods;
    }

    public void setDeclaredMethods(SoftMethod[] declaredMethods) {
        this.declaredMethods = declaredMethods;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public boolean hasBeanConstructor() {
        return beanConstructor;
    }

    public boolean isInterface() {
        return _isInterface;
    }
    
    public void setInterface(boolean i) {
        this._isInterface=i;
    }

    
}
