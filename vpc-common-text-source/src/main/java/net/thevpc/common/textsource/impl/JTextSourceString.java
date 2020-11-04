/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.textsource.impl;

import net.thevpc.common.textsource.JTextSource;
import java.io.StringReader;
import java.util.Collections;
import java.util.Iterator;

import net.thevpc.common.textsource.JTextSourceReport;
import net.thevpc.common.textsource.JTextSourceRoot;

/**
 *
 * @author vpc
 */
public class JTextSourceString implements JTextSourceRoot {
    
    private String value;
    private String sourceName;

    public JTextSourceString(String value, String sourceName) {
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
    public Iterable<JTextSource> iterate(JTextSourceReport log) {
        return new LogJSourceIterable(log) {
            @Override
            public Iterator<JTextSource> iterator() {
                return Collections.singleton((JTextSource) new DefaultJTextSource(sourceName,new StringReader(value), new TextCharSupplier(value))).iterator();
            }
        };
    }
    
}
