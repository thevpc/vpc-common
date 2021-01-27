package net.thevpc.common.classpath;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thevpc
 * %creationtime 12/16/12 1:00 PM
 */
public class URLClassIterable implements Iterable<Class> {

    protected static final Logger log = Logger.getLogger(URLClassIterable.class.getName());
    public URL[] urls;
    public ClassPathFilter configFilter;
    public ClassLoader classLoader;

    public URLClassIterable(URL[] urls, ClassPathFilter configFilter, ClassLoader classLoader) {
        this.urls = urls;
        this.configFilter = configFilter;
        this.classLoader = classLoader == null ? new URLClassLoader(urls) : classLoader;
    }


    public Iterator<Class> iterator() {
        return new URLClassIterableIterator(this);
    }

    Class configureClassURL(URL src, String path) throws ClassNotFoundException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith(".class")) {
            String cls = path.substring(0, path.length() - ".class".length()).replace('/', '.');
            String pck = null;
            int endIndex = cls.lastIndexOf('.');
            if (endIndex > 0) {
                pck = cls.substring(0, endIndex);
            }
            int dollar = cls.indexOf('$');
            boolean anonymousClass = false;
            boolean innerClass = false;
            if (dollar >= 0) {
                innerClass = true;
                //special class
                if (dollar + 1 < cls.length()) {
                    String subName = cls.substring(dollar + 1);
                    if (subName.length() > 0 && Character.isDigit(subName.charAt(0))) {
                        anonymousClass = true;
                    }
                }
            }
            if (configFilter == null || configFilter.acceptClassName(src, cls, anonymousClass)) {
                Class<?> aClass = null;
                try {
                    aClass = Class.forName(cls, false, classLoader);
                } catch (Throwable e) {
                    log.log(Level.FINE, "Unable to load class {0} for Classpath configuration. Ignored", cls);
                }
                if (aClass != null) {
                    if (configFilter == null || configFilter.acceptClass(src, cls, anonymousClass, aClass)) {
                        return (aClass);
                    }
                }
            }
        }
        return null;
    }

}
