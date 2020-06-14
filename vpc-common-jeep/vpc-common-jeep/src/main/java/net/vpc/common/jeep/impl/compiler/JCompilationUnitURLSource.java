/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import java.io.File;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;

import net.vpc.common.jeep.core.compiler.JSourceFactory;
import net.vpc.common.jeep.core.compiler.JSourceRoot;
import net.vpc.common.jeep.impl.io.URLCharSupplier;

/**
 *
 * @author vpc
 */
public class JCompilationUnitURLSource implements JSourceRoot {
    
    private URL url;

    public JCompilationUnitURLSource(URL url) {
        this.url = url;
        if (url == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getId() {
        return url.toString();
    }

    @Override
    public Iterable<JSource> iterate(JCompilerLog log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JSource> iterator() {
                if(url.getProtocol().equals("file")){
                    File f;
                    try {
                        f = Paths.get(url.toURI()).toFile();
                        return JSourceFactory.rootFile(f).iterator();
                    } catch (URISyntaxException ex) {
                        //ignore...
                    }
                }
                URL resource = url;
                if (resource != null) {
                    try {
                        InputStream in = resource.openStream();
                        if (in != null) {
                            return Collections.singleton((JSource) new DefaultJCompilationUnitSource(
                                    url.toString(),
                                    new InputStreamReader(in), new URLCharSupplier(resource))).iterator();
                        }
                    } catch (IOException e) {
                        if(log!=null){
                            log.error("Q000", null, e.getMessage() + ". url  not found : " + url, null);
                        }
                    }
                }
                return Collections.emptyIterator();
            }
        };
    }
    
}
