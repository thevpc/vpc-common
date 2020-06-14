/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

import net.vpc.common.classpath.ClassPathResource;
import net.vpc.common.classpath.ClassPathResourceFilter;
import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;
import net.vpc.common.jeep.core.compiler.JSourceRoot;
import net.vpc.common.jeep.impl.io.ZippedURLFileCharSupplier;

/**
 *
 * @author vpc
 */
public class JCompilationUnitURLFolderSource implements JSourceRoot {
    
    private URL url;

    private ClassPathResourceFilter filter;
    private String id;
    public JCompilationUnitURLFolderSource(URL url,String fileNameFilter) {
        this.url = url;
        this.filter = ClassPathResourceFilterByName.of(null, fileNameFilter);
        id=url+(fileNameFilter==null?"":(":"+fileNameFilter));
        if (url == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Iterable<JSource> iterate(JCompilerLog log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JSource> iterator() {
                Iterator<ClassPathResource> classPathResources = ClassPathUtils.resolveResources(new URL[]{url}, filter).iterator();
                return IteratorUtils.mapOptional(
                        classPathResources, new Function<ClassPathResource, Optional<JSource>>() {
                            @Override
                            public Optional<JSource> apply(ClassPathResource r) {
                                String currentPath = r.getPath();
                                try {
                                    return Optional.of(new DefaultJCompilationUnitSource(
                                            url.toString()+"?"+
                                                    r.getPath(),new InputStreamReader(r.open()), new ZippedURLFileCharSupplier(url, currentPath, filter)));
                                } catch (IOException e) {
                                    if (log != null) {
                                        log.error("Q000", null, e.getMessage() + ". url not found : " + url, null);
                                    }
                                }
                                return Optional.empty();
                            }
                        }
                );
            }
        };
    }
    
}
