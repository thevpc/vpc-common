/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author vpc
 */
public final class SoftMethodImpl implements SoftMethod{
    private String name;
    private int modifiers;
    private String returnType="???";
    private String[] parameterTypes;
    private SoftClass declaringClass;
    private String declaringClassName;
    private SoftAnnotation[] annotations;

    private static final int LANGUAGE_MODIFIERS = 
	Modifier.PUBLIC		| Modifier.PROTECTED	| Modifier.PRIVATE | 
	Modifier.ABSTRACT	| Modifier.STATIC	| Modifier.FINAL   |  
	Modifier.SYNCHRONIZED	| Modifier.NATIVE;

    public SoftMethodImpl() {
    }

    public SoftMethodImpl(Method method) {
        Annotation[] _annotations = method.getAnnotations();
        SoftAnnotation[] rAnnotations=new SoftAnnotation[_annotations.length];
        for (int i = 0; i < rAnnotations.length; i++) {
            rAnnotations[i]=new SoftAnnotationImpl(_annotations[i]);
        }
        setAnnotations(rAnnotations);

        final Class<?>[] parameterTypes0 = method.getParameterTypes();
        final String[] _parameterTypes = new String[parameterTypes0.length];
        for (int i = 0; i < _parameterTypes.length; i++) {
            _parameterTypes[i]=parameterTypes0[i].getName();
        }
        setParameterTypes(_parameterTypes);
        
        setName(method.getName());
        setReturnType(method.getReturnType().getName());
        setModifiers(method.getModifiers());
        setDeclaringClassName(method.getDeclaringClass().getName());
    }

    
    public SoftClass getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(SoftClass declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getDeclaringClassName() {
        return declaringClassName;
    }

    public void setDeclaringClassName(String declaringClassName) {
        this.declaringClassName = declaringClassName;
    }

    public SoftAnnotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(SoftAnnotation[] annotations) {
        this.annotations = annotations;
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

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    
    public String getReturnType(){
       return returnType; 
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public String toString() {
	try {
	    StringBuffer sb = new StringBuffer();
	    int mod = getModifiers() & LANGUAGE_MODIFIERS;
	    if (mod != 0) {
		sb.append(Modifier.toString(mod) + " ");
	    }
	    sb.append(simpleClassName(getReturnType()) + " ");
	    sb.append(simpleClassName(getDeclaringClass().getName()) + ".");
	    sb.append(getName() + "(");
	    String[] params = parameterTypes; // avoid clone
	    for (int j = 0; j < params.length; j++) {
		sb.append(simpleClassName(params[j]));
		if (j < (params.length - 1))
		    sb.append(",");
	    }
	    sb.append(")");
//	    Class[] exceptions = exceptionTypes; // avoid clone
//	    if (exceptions.length > 0) {
//		sb.append(" throws ");
//		for (int k = 0; k < exceptions.length; k++) {
//		    sb.append(exceptions[k].getName());
//		    if (k < (exceptions.length - 1))
//			sb.append(",");
//		}
//	    }
	    return sb.toString();
	} catch (Exception e) {
	    return "<" + e + ">";
	}
    }
    private String simpleClassName(String n){
        return n;
    }
}
