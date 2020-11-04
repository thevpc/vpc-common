/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface AnnotationFilter {

    /**
     * when true acceptMethodAnnotation and acceptFieldAnnotation will not be
     * invoked if acceptTypeAnnotation returns false
     *
     * @return
     */
    boolean isSupportedTypeAnnotation();

    boolean isSupportedMethodAnnotation();

    boolean isSupportedFieldAnnotation();

    boolean acceptTypeAnnotation(String name, String targetType, Class value);

    boolean acceptMethodAnnotation(String name, String targetMethod, String targetType, Method value);

    boolean acceptFieldAnnotation(String name, String targetField, String targetType, Field value);
}
