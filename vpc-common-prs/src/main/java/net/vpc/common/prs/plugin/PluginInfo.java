/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.prs.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author vpc
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PluginInfo {
    String messageSet() default "";
    String iconSet() default "";
    String id() default "";
    String category() default "";
    String version() default "";
    String author() default "";
    String contributors() default "";
    String description() default "";
    DBCPluginDependency[] dependencies() default {};
}
