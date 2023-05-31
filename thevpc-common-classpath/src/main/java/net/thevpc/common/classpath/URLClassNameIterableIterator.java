/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;

/**
 *
 * @author taha.bensalah@gmail.com
 */
class URLClassNameIterableIterator implements Iterator<String> {
    private final URLClassNameIterable outer;
    private String nextType;
    private int urlIndex = 0;
    private Iterator<ClassPathResource> classPathResources;

    public URLClassNameIterableIterator(final URLClassNameIterable outer) {
        this.outer = outer;
    }

    public boolean hasNext() {
        while (urlIndex < outer.urls.length) {
            URL url = outer.urls[urlIndex].url;
            if(outer.urls[urlIndex].cpr==null){
                URLClassIterable.log.log(Level.FINE, "ignoring  configuration from url : {0}", url);
                urlIndex++;
            }else {
                if (classPathResources == null) {
                    if (outer.configFilter == null || outer.configFilter.acceptLibrary(url)) {
                        URLClassIterable.log.log(Level.FINE, "configuration from  url : {0}", url);
                        classPathResources = outer.urls[urlIndex].cpr.get();
                    } else {
                        urlIndex++;
                        URLClassIterable.log.log(Level.FINE, "ignoring  configuration from url : {0}", url);
                        continue;
                    }
                }
                if (classPathResources.hasNext()) {
                    String c = null;
                    try {
                        ClassPathResource cr = classPathResources.next();
                        c = outer.configureClassURL(url, cr.getPath());
                    } catch (ClassNotFoundException ex) {
                        URLClassIterable.log.log(Level.SEVERE, null, ex);
                    }
                    if (c != null) {
                        nextType = c;
                        return true;
                    }
                } else {
                    classPathResources = null;
                    urlIndex++;
                }
            }
        }
        classPathResources = null;
        outer.close();
        return false;
    }

    public String next() {
        return nextType;
    }
    
}
