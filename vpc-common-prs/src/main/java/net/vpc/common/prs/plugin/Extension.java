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
 * 
 * Extension annotation defines an extension point for the plugin architecture. 
 * 
 * It must be used on an interface that should be implemented by plugin implementors. 
 * It will be detected automatically by plugin manager when parsing plugin jar files. 
 * This means that every plugin can either implement existing Extensions (defined in depedent plugins) 
 * of defines new extension points (that shoud be implemented by other plugins that will depends obviously on this one).
 * 
 * @author Taha BEN SALAH
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Extension {
    /**
     * group defines a way to organize extension into different groups.
     * this helps to filter extensions acording to their group
     * @return group id
     */
    String group() default "";


    /**
     * true if this interface is to be registered in the Factory to create instances.
     * cutomizable extension are extensions which implementations must be set up according to a user preferences instead of a priority base selector.
     * default value is true
     * @return true if this interface is customizable
     */
    boolean customizable() default true;

    /**
     * extensions that share the same implementations
     * This is help
     * @return Class array
     */
    Class[] shares() default {};
}