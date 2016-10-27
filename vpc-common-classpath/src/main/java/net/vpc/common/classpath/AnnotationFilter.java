/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.classpath;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface AnnotationFilter {

    /**
     * when true acceptMethodDecoration and acceptFieldDecoration will not be
     * invoked if acceptTypeDecoration returns false
     *
     * @return
     */
    boolean isSupportedTypeDecoration();

    boolean isSupportedMethodDecoration();

    boolean isSupportedFieldDecoration();

    boolean acceptTypeDecoration(String name, String targetType, Class value);

    boolean acceptMethodDecoration(String name, String targetMethod, String targetType, Method value);

    boolean acceptFieldDecoration(String name, String targetField, String targetType, Field value);
}
