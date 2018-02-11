/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.classpath.test;

import net.vpc.common.classpath.ClassPathUtils;
import org.junit.Test;

/**
 *
 * @author vpc
 */
public class Example {
    @Test
    public void test_resolveContextClassNamesList(){
        for (String clsName : ClassPathUtils.resolveContextClassNamesList()) {
            System.out.println(clsName); 
        }
    }
    @Test
    public void test_resolveContextClasses(){
        for (Class cls : ClassPathUtils.resolveContextClasses()) {
            System.out.println(cls); 
        }
    }
}
