package net.vpc.common.textsource.impl;

import net.vpc.common.textsource.JTextSource;
import net.vpc.common.textsource.JTextSourceReport;

public abstract class LogJSourceIterable implements Iterable<JTextSource>{
    protected JTextSourceReport log;

    public LogJSourceIterable(JTextSourceReport log) {
        this.log = log;
    }
}
