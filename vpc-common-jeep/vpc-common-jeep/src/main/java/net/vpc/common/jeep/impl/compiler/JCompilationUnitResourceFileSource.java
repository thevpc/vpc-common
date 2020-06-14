/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;
import net.vpc.common.jeep.core.compiler.JSourceRoot;
import net.vpc.common.jeep.impl.io.URLCharSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author vpc
 */
public class JCompilationUnitResourceFileSource implements JSourceRoot {

    String path;

    public JCompilationUnitResourceFileSource(String path) {
        this.path = path;
        if (path == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getId() {
        return path;
    }

    @Override
    public Iterable<JSource> iterate(JCompilerLog log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JSource> iterator() {
                String path2 = path;
                if (path2.startsWith("/")) {
                    path2 = path2.substring(1);
                }
                URL resource = ClassLoader.getSystemClassLoader().getResource(path2);
                if (resource != null) {
                    try {
                        InputStream in = resource.openStream();
                        if (in != null) {
                            return Collections.singleton((JSource) new DefaultJCompilationUnitSource("resource://" + path2, new InputStreamReader(in), new URLCharSupplier(resource))).iterator();
                        }else{
                            if (log != null) {
                                log.error("Q000", null, "resource not found : " + path, null);
                            }
                        }
                    } catch (IOException ioException) {
                        if (log != null) {
                            log.error("Q000", null, ioException.getMessage() + ". resource not found : " + path, null);
                        }
                    }
                } else {
                    if (log != null) {
                        log.error("Q000", null, "resource not found : " + path, null);
                    }
                }
                return Collections.emptyIterator();
            }
        };
    }

}
