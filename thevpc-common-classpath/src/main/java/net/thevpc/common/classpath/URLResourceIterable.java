package net.thevpc.common.classpath;

import java.net.URL;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author thevpc
 * %creationtime 12/16/12 1:00 PM
 */
public class URLResourceIterable implements Iterable<ClassPathResource> {

    protected static final Logger log = Logger.getLogger(URLResourceIterable.class.getName());
    public URL[] urls;
    public ClassPathResourceFilter configFilter;
    public ClassLoader contextClassLoader;

    public URLResourceIterable(URL[] urls, ClassPathResourceFilter configFilter) {
        this.urls = urls;
        this.configFilter = configFilter;
        /**
         * @PortabilityHint(target="C#",name="suppress")
         */
        this.contextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    public Iterator<ClassPathResource> iterator() {
        return new URLResourceIterableIterator(this);
    }

}
