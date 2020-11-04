package net.thevpc.common.textsource.impl;

import net.thevpc.common.textsource.JTextSource;
import net.thevpc.common.textsource.JTextSourceReport;

public abstract class LogJSourceIterable implements Iterable<JTextSource>{
    protected JTextSourceReport log;

    public LogJSourceIterable(JTextSourceReport log) {
        this.log = log;
    }
}
