package net.vpc.common.jeep.impl.compiler;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;

public abstract class LogJSourceIterable implements Iterable<JSource>{
    protected JCompilerLog log;

    public LogJSourceIterable(JCompilerLog log) {
        this.log = log;
    }
}
