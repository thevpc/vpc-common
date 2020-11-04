/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.net.URL;
import java.util.Iterator;

/**
 *
 * @author taha.bensalah@gmail.com
 */
class URLResourceIterableIterator implements Iterator<ClassPathResource> {
    private final URLResourceIterable outer;
    private ClassPathResource nextType;
    private int urlIndex = 0;
    private Iterator<ClassPathResource> classPathResources;

    public URLResourceIterableIterator(final URLResourceIterable outer) {
        this.outer = outer;
    }

    public boolean hasNext() {
        while (urlIndex < outer.urls.length) {
            URL jarURL = outer.urls[urlIndex];
            if (classPathResources == null) {
                if (outer.configFilter==null || outer.configFilter.acceptLibrary(jarURL)) {
                    classPathResources = new ClassPathRoot(jarURL).iterator();
                } else {
                    urlIndex++;
                    continue;
                }
            }
            if (classPathResources.hasNext()) {
                ClassPathResource cr = classPathResources.next();
                if (cr != null) {
                    if (outer.configFilter==null || outer.configFilter.acceptResource(cr.getPath())) {
                        nextType = cr;
                        return true;
                    }
                }
            } else {
                classPathResources = null;
                urlIndex++;
            }
        }
        classPathResources = null;
        return false;
    }

    public ClassPathResource next() {
        return nextType;
    }
    
}
