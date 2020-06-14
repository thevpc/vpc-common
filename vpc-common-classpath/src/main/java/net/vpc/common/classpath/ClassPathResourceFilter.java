/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.classpath;

import java.net.URL;

/**
 * @author taha.bensalah@gmail.com
 */
public interface ClassPathResourceFilter {

    boolean acceptLibrary(URL url);

    boolean acceptResource(String url);
}
