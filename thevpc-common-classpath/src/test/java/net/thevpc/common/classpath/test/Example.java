///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.common.classpath.test;
//
//import net.thevpc.common.classpath.ClassPathFilter;
//import net.thevpc.common.classpath.ClassPathUtils;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.net.URL;
//
///**
// *
// * @author thevpc
// */
//public class Example {
//    @Test
//    public void test_resolveFolderClassNamesList(){
//        try {
//            for (String clsName : ClassPathUtils.resolveClassNames(
//                    new URL[]{
//                            new File("/data/public/git/scholar-mw/hadrumaths/hadrumaths/target/classes/").toURI().toURL()
//                    },(ClassPathFilter) null
//            )) {
//                System.out.println(clsName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void test_resolveContextClassNamesList(){
//        for (String clsName : ClassPathUtils.resolveContextClassNamesList(true)) {
//            System.out.println(clsName);
//        }
//    }
//    @Test
//    public void test_resolveContextClasses(){
//        for (Class cls : ClassPathUtils.resolveContextClasses(true)) {
//            System.out.println(cls);
//        }
//    }
//}
