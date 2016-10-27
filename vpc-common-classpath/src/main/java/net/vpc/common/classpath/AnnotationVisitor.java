/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.classpath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author vpc
 */
public interface AnnotationVisitor {

    void visitTypeAnnotation(Annotation annotation, Class clazz);

    void visitMethodAnnotation(Annotation annotation, Method method);

    void visitFieldAnnotation(Annotation annotation, Field method);
}
