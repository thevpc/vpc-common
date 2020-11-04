package net.thevpc.common.classpath;

import java.net.URL;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author Taha BEN SALAH <taha.bensalah@gmail.com>
 * @creationdate 12/16/12 1:00 PM
 */
public class URLClassNameIterable implements Iterable<String> {

    protected static final Logger log = Logger.getLogger(URLClassNameIterable.class.getName());
    public URL[] urls;
    public ClassPathFilter configFilter;
    public ClassLoader contextClassLoader;

    public URLClassNameIterable(URL[] urls, ClassPathFilter configFilter) {
        this.urls = urls;
        this.configFilter = configFilter;
        /**
         * @PortabilityHint(target="C#",name="suppress")
         */
        this.contextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    public Iterator<String> iterator() {
        return new URLClassNameIterableIterator(this);
    }

    String configureClassURL(URL src, String path) throws ClassNotFoundException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
//        URL url = classLoader.getResource(path);
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
                return cls;
            } else {
//                      System.out.println(path);
//                      System.out.println("\tSOURCE  " + src);
//                      System.out.println("\tSYS URL " + sysURL);
//                      System.out.println("\tAPP URL " + url);
//                      System.out.println("\tPKG   " + (pck==null?"":Package.getPackage(pck)));
//                      System.out.println("\tCLASS " + Class.forName(cls,false,classLoader));
            }
        }
        return null;
    }

}
