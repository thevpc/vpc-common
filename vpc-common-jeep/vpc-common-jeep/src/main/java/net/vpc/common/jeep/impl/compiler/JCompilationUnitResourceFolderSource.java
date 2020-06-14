/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

import net.vpc.common.classpath.ClassPathResource;
import net.vpc.common.classpath.ClassPathResourceFilter;
import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;
import net.vpc.common.jeep.core.compiler.JSourceRoot;
import net.vpc.common.jeep.impl.io.ContextClassLoaderCharSupplier;

/**
 *
 * @author vpc
 */
public class JCompilationUnitResourceFolderSource implements JSourceRoot {

    private final String path;
    private final ClassPathResourceFilter filter;
    private final String id;

    public JCompilationUnitResourceFolderSource(String path, String fileNameFilter) {
        this.path = path;
        this.filter = ClassPathResourceFilterByName.of(path, fileNameFilter);
        id=path+(fileNameFilter==null?"":(":"+fileNameFilter));
        if (path == null) {
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
                String path2 = path;
                if (path2.startsWith("/")) {
                    path2 = path2.substring(1);
                }
                Iterator<ClassPathResource> classPathResources = ClassPathUtils.resolveContextResources(filter, false).iterator();
                return IteratorUtils.mapOptional(classPathResources, new Function<ClassPathResource, Optional<JSource>>() {
                    @Override
                    public Optional<JSource> apply(ClassPathResource r) {
                        try {
                            return Optional.of(
                                    new DefaultJCompilationUnitSource(JCompilationUnitResourceFolderSource.this.path+"/"+r.getPath(),new InputStreamReader(r.open()), new ContextClassLoaderCharSupplier(r.getPath(), filter))
                            );
                        } catch (IOException e) {
                            if (log != null) {
                                log.error("Q000", null, e.getMessage() + ". resource not found : " + path, null);
                            }
                            return Optional.empty();
                        }
                    }
                });
            }
        };
    }

}
