/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.classpath;

import java.net.URL;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface ClassPathFilter {

    public boolean acceptLibrary(URL url);

    public boolean acceptClassName(URL url, String className);

    public boolean acceptClass(URL url, String className, Class type);
}
