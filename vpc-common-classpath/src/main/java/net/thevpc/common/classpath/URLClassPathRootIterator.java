/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class URLClassPathRootIterator implements Iterator<ClassPathResource> {

    private URL url;
    private ZipInputStream jar;
    private ZipEntry nextEntry;

    public URLClassPathRootIterator(URL url) throws IOException {
        this.url = url;
        /**
         * @PortabilityHint(target = "C#", name = "ignore")
         */
        jar = new JarInputStream(url.openStream());
    }

    public boolean hasNext() {
        /**
         * @PortabilityHint(target = "C#", name = "todo")
         * return false;
         */
        {
            try {
                nextEntry = jar.getNextEntry();
            } catch (IOException ex) {
                return false;
            }
            return nextEntry != null;
        }
    }

    public ClassPathResource next() {
        /**
         * @PortabilityHint(target = "C#", name = "todo")
         * return null;
         */
        return new ZipEntryClassPathResource(nextEntry.getName(), nextEntry, jar);
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected void finalize() throws Throwable {
        if (jar != null) {
            jar.close();
        }
        super.finalize();
    }

}
