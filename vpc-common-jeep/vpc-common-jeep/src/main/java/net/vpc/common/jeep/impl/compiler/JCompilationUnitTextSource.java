/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;
import java.io.StringReader;
import java.util.Collections;
import java.util.Iterator;

import net.vpc.common.jeep.core.compiler.JSourceRoot;
import net.vpc.common.jeep.impl.io.TextCharSupplier;

/**
 *
 * @author vpc
 */
public class JCompilationUnitTextSource implements JSourceRoot {
    
    private String value;
    private String sourceName;

    public JCompilationUnitTextSource(String value,String sourceName) {
        this.value = value;
        this.sourceName = sourceName==null?"<text>":sourceName;
        if (value == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getId() {
        return sourceName;
    }

    @Override
    public Iterable<JSource> iterate(JCompilerLog log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JSource> iterator() {
                return Collections.singleton((JSource) new DefaultJCompilationUnitSource(sourceName,new StringReader(value), new TextCharSupplier(value))).iterator();
            }
        };
    }
    
}
