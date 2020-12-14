/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.softreflect;

import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thevpc
 */
public class HostToSoftClass implements SoftClass {

    private SoftClassLoader classLoader;
    private Class clazz;
    private static final Class[] classes0 = new Class[0];

    public HostToSoftClass(Class clazz,SoftClassLoader classLoader) {
        this.clazz = clazz;
        this.classLoader = classLoader;
    }

    public String getName() {
        return clazz.getName();
    }

    public int getModifiers() {
        return clazz.getModifiers();
    }

    public boolean isInterface() {
        return clazz.isInterface();
    }

    public boolean hasBeanConstructor() {
        try {
            clazz.getConstructor(classes0);
            if (!Modifier.isPublic(clazz.getModifiers())) {
                return false;
            }
            //that's perfect, a default constructor found!
            return true;
        } catch (Throwable ex) {
            //
        }
        return false;
    }

    public SoftClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(SoftClassLoader loader) {
        this.classLoader=loader;
    }
    
    

    public SoftField[] getDeclaredFields() {
        Field[] fields = clazz.getDeclaredFields();
        SoftField[] rfields = new SoftField[fields.length];
        for (int i = 0; i < rfields.length; i++) {
            Field field = fields[i];
            rfields[i]=new SoftFieldImpl(field);
        }
        return rfields;
    }

    public SoftMethod[] getDeclaredMethods() {
        Method[] fields = clazz.getDeclaredMethods();
        SoftMethod[] rfields = new SoftMethod[fields.length];
        for (int i = 0; i < rfields.length; i++) {
            SoftMethodImpl mm = new SoftMethodImpl(fields[i]);
            mm.setDeclaringClass(this);
            rfields[i] = mm;
        }
        return rfields;
    }

    public String[] getInterfaces() {
        final Class[] types = clazz.getInterfaces();
        final String[] names = new String[types.length];
        for (int i = 0; i < names.length; i++) {
            names[i]=types[i].getName();
        }
        return names;
    }

    public String getSuperclass() {
        Class superclass = clazz.getSuperclass();
        return superclass==null?null:superclass.getName();
    }
    
    public SoftAnnotation[] getAnnotations() {
        List<SoftAnnotation> annotations=new ArrayList<SoftAnnotation>();
        for (Annotation annotation : clazz.getAnnotations()) {
            SoftAnnotationImpl a=new SoftAnnotationImpl(annotation);
            annotations.add(a);
        }
        return annotations.toArray(new SoftAnnotation[annotations.size()]);
    }
    
    
    
}
