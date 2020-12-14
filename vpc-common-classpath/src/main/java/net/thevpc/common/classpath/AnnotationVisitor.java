/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author thevpc
 */
public interface AnnotationVisitor {

    void visitClassAnnotation(Annotation annotation, Class clazz);

    void visitMethodAnnotation(Annotation annotation, Method method);

    void visitFieldAnnotation(Annotation annotation, Field method);
}
