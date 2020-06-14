/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;
import net.vpc.common.jeep.core.compiler.JSourceRoot;
import net.vpc.common.jeep.impl.io.FileCharSupplier;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author vpc
 */
public class JCompilationUnitFileSource implements JSourceRoot {

    private File file;
    private String id;

    public JCompilationUnitFileSource(File file) {
        this.file = file;
        if (file == null) {
            throw new NullPointerException();
        }
        id = file.getAbsolutePath();
        try {
            id = file.getCanonicalPath();
        } catch (Exception ex) {
            //ignore.
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public Iterable<JSource> iterate(JCompilerLog log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JSource> iterator() {
                if (!file.isFile() && !file.isDirectory()) {
                    if (log != null) {
                        log.error("Q000", null, "file not found : " + file.getPath(), null);
                    }
                }
                if (file.isFile()) {
                    try {
                        return Collections.singleton((JSource) new DefaultJCompilationUnitSource(file.getPath(), new FileReader(file), new FileCharSupplier(file))).iterator();
                    } catch (FileNotFoundException e) {
                        if (log != null) {
                            log.error("Q000", null, e.getMessage() + " : " + file.getPath(), null);
                        }
                    }
                }
                try {
                    return Files.walk(file.toPath()).filter((x) -> Files.isRegularFile(x)).map((x) -> x.toFile()).filter((x) -> x.getName().endsWith(".hl")).map((x) -> {
                        try {
                            return (JSource) new DefaultJCompilationUnitSource(x.toString(), new FileReader(x), new FileCharSupplier(x));
                        } catch (FileNotFoundException e) {
                            if (log != null) {
                                log.error("Q000", null, e.getMessage() + " : " + file.getPath(), null);
                            }
                            return null;
                        }
                    }).filter(Objects::nonNull).iterator();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
    }

}
